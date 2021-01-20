package com.tc_4.carbon_counter.sockets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.tc_4.carbon_counter.databases.NotificationDatabase;
import com.tc_4.carbon_counter.databases.UserDatabase;
import com.tc_4.carbon_counter.exceptions.UserNotFoundException;
import com.tc_4.carbon_counter.models.Notification;
import com.tc_4.carbon_counter.models.User;
import com.tc_4.carbon_counter.models.User.Notifications;
import com.tc_4.carbon_counter.models.User.Role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller      // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/notify/{username}")  // this is Websocket url
public class NotificationSocket {

    // cannot autowire static directly (instead we do it by the below
    // method
    private static NotificationDatabase notificationDatabase; 
    
    private static UserDatabase userDatabase;

	/*
   * Grabs the MessageRepository singleton from the Spring Application
   * Context.  This works because of the @Controller annotation on this
   * class and because the variable is declared as static.
   * There are other ways to set this. However, this approach is
   * easiest.
	 */
	@Autowired
	public void setNotificationDatabase(NotificationDatabase database) {
		notificationDatabase = database;  // we are setting the static variable
	}

    @Autowired
	public void setUserDatabase(UserDatabase database) {
		userDatabase = database;  // we are setting the static variable
    }
    
	// Store all socket session and their corresponding username.
	private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
	private static Map<String, Session> usernameSessionMap = new Hashtable<>();

	private final Logger logger = LoggerFactory.getLogger(NotificationSocket.class);

    /**
     * Preform these actions when a user connections to the web socket.
     * Send any unread notifications.
     * 
     * @param session the websocket session, automatically provided
     * @param username username, provided as a path parameter in the websocket url
     * @throws IOException if could not make a good connection
     */
	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username) 
      throws IOException {

		logger.info( username + " has opened a connection");

        // store connecting user information
		sessionUsernameMap.put(session, username);
		usernameSessionMap.put(username, session);

        //Send any unread notifications to the newly connected user
        sendUnreadNotificationsToUser(username);
	}

    /**
     * Preform these actions when the server receives a message a message from a user.
     * Begin a message with "@username" to send the notification to username.
     * Begin a message with "@ROLE" to send a notification to all users of that role.
     * note all caps for the roll. USER, CREATOR, ADMIN, DEV
     * 
     * @param session websocket session, automatically provided
     * @param message the message that has been received
     * @throws IOException if connection error occurs
     */
	@OnMessage
	public void onMessage(Session session, String message) throws IOException {
        String toUsername = "";
        // Handle new messages
        logger.info("Received Message: " + message);
        
        if (message.startsWith("@")) {
            toUsername = message.split(" ")[0].substring(1); 
            message = message.substring(toUsername.length() + 2);
        }

        Notification n = new Notification();

        if(toUsername.equals(Role.USER.toString()) || toUsername.equals(Role.CREATOR.toString()) || toUsername.equals(Role.ADMIN.toString()) || toUsername.equals(Role.DEV.toString())){
            n.setUsername(toUsername);
            n.setIsRead(false);
            n.setMessage(message);
            sendNotificationToRole(n);
        }
        else if(toUsername != ""){
            n.setUsername(toUsername);
            n.setIsRead(false);
            n.setMessage(message);

            sendNotificationToUser(n);
        }
        else { // broadcast
            n.setMessage(message);
            n.setUsername("");
            n.setIsRead(false);

			broadcast(n);
		}

	}

    /**
     * Preform these actions when a user disconnects from the websocket
     * 
     * @param session   the websocket session, provided automatically
     * @throws IOException if a communication error occurs
     */
	@OnClose
	public void onClose(Session session) throws IOException {
        String username = sessionUsernameMap.get(session);

        logger.info(username + " has closed the connection");

        // remove the user connection information
		sessionUsernameMap.remove(session);
		usernameSessionMap.remove(username);
	}


	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
		logger.info("Error");
		throwable.printStackTrace();
	}

    /**
     * Send a notification to a specific user if they have notifications turned on.
     * If the user is not currently connected, will store the notification 
     * until they next connect.
     * 
     * @param message the message to send
     * @param username the name of the user to send the notification to
     */
    public void sendNotificationToUser(String message, String username){
        Notification n = new Notification();
        n.setIsRead(false);
        n.setMessage(message);
        n.setUsername(username);
        sendNotificationToUser(n);
    }

    /**
     * Send a notification to a specific user if they have notifications turned on.
     * If the user is not currently connected, will store the notification 
     * until they next connect.
     * 
     * @param notification The notification to send, holds username and message info
     */
	public void sendNotificationToUser(Notification notification) {
        Optional<User> optionalUser = userDatabase.findByUsername(notification.getUsername());
        if(!optionalUser.isPresent()){
            throw new UserNotFoundException(notification.getUsername());
        }

        User user = optionalUser.get();
        //if notifications are off, dont save to database
        if(user.getNotifications() == Notifications.OFF){
            return;
        }

        Session session = usernameSessionMap.get(notification.getUsername());

        //user is not connected
        if(notification.getUsername() != "" && session == null){
            notification.setIsRead(false);
            notificationDatabase.save(notification);
        //user is connected
        }else{
            try {
                session.getBasicRemote().sendText(notification.getMessage());
                notification.setIsRead(true);
                notificationDatabase.save(notification);
            } 
            catch (IOException e) {
                logger.info("Exception: " + e.getMessage().toString());
                e.printStackTrace();
            }
        }
    }
    
    public void sendNotificationToRole(Notification notification){
        List<User> listToSend = new ArrayList<User>();
        switch(notification.getUsername()){
            case "USER":
                listToSend = userDatabase.findByRole(Role.USER);
                break;
            case "CREATOR":
                listToSend = userDatabase.findByRole(Role.CREATOR);
                break;
            case "ADMIN":
                listToSend = userDatabase.findByRole(Role.ADMIN);
                break;
            case "DEV":
                listToSend = userDatabase.findByRole(Role.DEV);
                break;
        }

        for(User u : listToSend){
            Notification n = new Notification();
            n.setIsRead(false);
            n.setMessage(notification.getMessage());
            n.setUsername(u.getUsername());
            sendNotificationToUser(n);
        }
       
    }

    /**
     * Send a notification to all currently connected users
     * 
     * @param message the message to send
     */
    public void broadcast(String message) {
        Notification n = new Notification();
        n.setIsRead(false);
        n.setUsername("");
        n.setMessage(message);
        broadcast(n);
    }

    /**
     * send a notification to all currently connected users
     * 
     * @param notification the notification to send, holds message info
     */
	public void broadcast(Notification notification) {
        notification.setUsername("");
        notificationDatabase.save(notification);
		sessionUsernameMap.forEach((session, username) -> {
			try {
                session.getBasicRemote().sendText(notification.getMessage());
			} 
            catch (IOException e) {
				logger.info("Exception: " + e.getMessage().toString());
				e.printStackTrace();
			}
		});

    }
    
    /**
     * Send all unread notifications to a specific user if they 
     * have notifications turned on.
     * 
     * @param username the name of the user to send the notifications to.
     */
    private void sendUnreadNotificationsToUser(String username){
        List<Notification> notifications = notificationDatabase.findByUsernameAndIsRead(username, false);
        for(Notification n: notifications){
            sendNotificationToUser(n);
        }
    }

}