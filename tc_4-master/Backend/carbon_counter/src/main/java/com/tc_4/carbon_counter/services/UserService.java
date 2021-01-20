package com.tc_4.carbon_counter.services;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.tc_4.carbon_counter.databases.FriendsDatabase;
import com.tc_4.carbon_counter.databases.UserDatabase;
import com.tc_4.carbon_counter.exceptions.RequestExistsException;
import com.tc_4.carbon_counter.exceptions.RequestNotFoundException;
import com.tc_4.carbon_counter.exceptions.UnauthorizedException;
import com.tc_4.carbon_counter.exceptions.UserNotFoundException;
import com.tc_4.carbon_counter.models.Friends;
import com.tc_4.carbon_counter.models.User;
import com.tc_4.carbon_counter.models.Friends.Status;
import com.tc_4.carbon_counter.models.User.Mode;
import com.tc_4.carbon_counter.models.User.Notifications;
import com.tc_4.carbon_counter.models.User.Role;
import com.tc_4.carbon_counter.sockets.NotificationSocket;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service to handle the logic when dealing with users. Able to get users, add
 * users, remove users etc. This class is made to be called from controllers.
 * 
 * @author Colton Glick
 * @author Andrew Pester
 */
@Service
public class UserService {

    @Autowired
    private UserDatabase userDatabase;

    @Autowired
    private FriendsDatabase friendsDatabase;

    @Autowired
    private NotificationSocket notification;

    /** Password encoder used when making a new user or changing a password */
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Get a user object from the database based on their username. Must have at
     * least admin permissions or authenticated as this user.
     * 
     * @param username user name of the user to get
     * @return User object
     * @throws UserNotFoundException
     */
    public User getUser(String username) {
        if (User.checkPermission(Role.ADMIN)) {
            return userDatabase.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        } else {
            if (SecurityContextHolder.getContext().getAuthentication().getName().equals(username)) {
                return userDatabase.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
            }
            // else unauthorized use
            throw new UnauthorizedException("You do not have permission to access user '" + username + "'");
        }
    }

    /**
     * Add a user to the database. All user variables are required
     * 
     * @param user to add
     * @return the user that was added to the database
     */
    public User addUser(User user) {
        // encrypt password when saving to the database
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userDatabase.save(user);
    }

    /**
     * Change the password of the given user. Must have at least admin permissions
     * or be authenticated as the user to change.
     * 
     * @param username    The user name of the user to change
     * @param newPassword The new password to use, pass as plain text, will be
     *                    encrypted before saving
     * @return boolean, true if the password change was successful
     * @throws UnauthorizedException if you don't have permission to change this
     *                               user's password
     */
    public boolean changePassword(String username, String newPassword) {
        if (!User.checkPermission(Role.ADMIN)
                && !SecurityContextHolder.getContext().getAuthentication().getName().equals(username)) {
            throw new UnauthorizedException(
                    "You do not have permission to change the password of user '" + username + "'");
        }

        // must have the original password which is oldPassword to change password to
        // password with this setup
        User user = getUser(username);

        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            // encrypt new password
            user.setPassword(passwordEncoder.encode(newPassword));
            userDatabase.save(user);
            return true;
        }
        throw new UnauthorizedException("Incorrect old password");
    }

    /**
     * 
     * @param user     the user sending the friend request
     * @param username the user receiving the request
     * @return true if it sends the request otherwise throws userNotFoundException
     *         or RequestExistsException
     */
    public boolean friendRequest(String user, String username) {
        if (!userDatabase.existsByUsername(user)) {
            throw new UserNotFoundException(user);
        }
        if (!userDatabase.existsByUsername(username)) {
            throw new UserNotFoundException(username);
        }
        Friends temp = new Friends();
        temp.setUserOne(user);
        temp.setUserTwo(username);
        temp.setStatus(Status.REQUESTED);
        if(friendsDatabase.findByUserOneAndUserTwo(user, username).isPresent()){
            throw new RequestExistsException();
        }else if(friendsDatabase.findByUserOneAndUserTwo(username, user).isPresent()){
            throw new RequestExistsException();
        }

        if (!friendsDatabase.findByUserOneAndUserTwo(user, username).isPresent()) {
            friendsDatabase.save(temp);
            notification.sendNotificationToUser(user + " has sent you a friend request!", username);
            return true;
        }
        throw new RequestExistsException();
    }

    /**
     * 
     * @param username the username of the user
     * @return all the friend request of that user
     */
    public List<Friends> allFriendRequests(String username) {
        return friendsDatabase.findByUserTwoAndStatus(username, Status.REQUESTED);
    }

    /**
     * Get a set of all approved friends for the currently 
     * authenticated user.
     * 
     * @return A set of usernames
     */
    public Set<String> getFriendList(){
        String thisUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Set<String> result = new TreeSet<String>();

        List<Friends> list1 = friendsDatabase.findByUserOneAndStatus(thisUsername, Status.APPROVED);
        for(Friends f : list1){
            result.add(f.getUserTwo());
        }

        List<Friends> list2 = friendsDatabase.findByUserTwoAndStatus(thisUsername, Status.APPROVED);
        for(Friends f : list2){
            result.add(f.getUserOne());
        }
        
        return result;
    }

    /**
     * 
     * @param username the user who is accepting the friend request
     * @param userOne  the user who sent the friend request
     * @return true if the request exists otherwise throws usernotfoundexception or
     *         requestnotfoundexception
     */
    public boolean acceptFriend(String username, String userOne) {
        if (friendsDatabase.findByUserOneAndUserTwo(userOne, username).isPresent()) {
            Friends temp = friendsDatabase.findByUserOneAndUserTwo(userOne, username).get();
            temp.setStatus(Status.APPROVED);
            friendsDatabase.save(temp);
            notification.sendNotificationToUser(username + " has accepted your friend request!", userOne);
            return true;
        } else if (!userDatabase.existsByUsername(username)) {
            throw new UserNotFoundException(username);
        } else if (!userDatabase.existsByUsername(userOne)) {
            throw new UserNotFoundException(userOne);
        } else {
            throw new RequestNotFoundException(userOne, username);
        }
    }

    /**
     * 
     * @param username the user who is denying the friend request
     * @param userOne  the user who sent the friend request
     * @return true if the request exists otherwise throws usernotfoundexception or
     *         requestnotfoundexception
     */
    public boolean denyFriend(String username, String userOne) {
        if (friendsDatabase.findByUserOneAndUserTwo(userOne, username).isPresent()) {
            Friends temp = friendsDatabase.findByUserOneAndUserTwo(userOne, username).get();
            temp.setStatus(Status.DENIED);
            friendsDatabase.save(temp);
            notification.sendNotificationToUser(username + " has denied your friend request", userOne);
            return true;
        } else if (!userDatabase.existsByUsername(username)) {
            throw new UserNotFoundException(username);
        } else if (!userDatabase.existsByUsername(userOne)) {
            throw new UserNotFoundException(userOne);
        } else {
            throw new RequestNotFoundException(userOne, username);
        }
    }

    public boolean setSettings(String username, String settings){
        //DONE
        JSONObject temp = new JSONObject(settings);
        if(temp.has("notifications") && !temp.isNull("notifications")){
            userDatabase.findByUsername(username).get().setNotifications(Notifications.valueOf(temp.getString("notifications")));
        }else if(temp.has("mode") && !temp.isNull("mode")){
            userDatabase.findByUsername(username).get().setMode(Mode.valueOf(temp.getString("mode")));
        }
        userDatabase.save(userDatabase.findByUsername(username).get());
        return true;

    }
    
    /**
     * Checks if the given user name exists in the user
     * database, if it does, returns true. If not, throws
     * UserNotFoundException
     * 
     * @param username  the user name to test
     * @return          true if the user does exists
     * 
     * @throws UserNotFoundException if the user does not exist
     */
    public boolean doesUserExist(String username) throws UserNotFoundException{
        if (userDatabase.existsByUsername(username)){
            return true;
        }else{
            throw new UserNotFoundException(username);
        }
    }

    /**
     * Edit the user's information with the passed in information.
     * Must have at least admin permissions or be authenticated as the user to change
     * 
     * @param username      The current username of the user to edit
     * @param userChanges   A user object that *holds only the changes to be made* null variables will be unchanged
     * @return              the new user after edits have been made
     */
    public User editUser(String username, User userChanges){
        User user = getUser(username);

        //if changing password 
        if(userChanges.getPassword()!=null){
            changePassword(username, userChanges.getPassword());
        }
        //copy all other values over
        user.copyFrom(userChanges);

        userDatabase.save(user);
        return user;
    }

    /**
     * Remove the specified user from the user database.
     * 
     * @param username  The username of the user to remove
     * @return          boolean, true if the user was successfully removed
     */
    public boolean removeUser(String username){
        userDatabase.delete(getUser(username));
        //TODO: delete all user stats too
        return true;
    }
}
