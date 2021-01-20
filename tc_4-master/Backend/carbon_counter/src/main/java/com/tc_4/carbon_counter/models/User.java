package com.tc_4.carbon_counter.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.tc_4.carbon_counter.security.CarbonUserPrincipal;

import org.springframework.security.core.context.SecurityContextHolder;

@Entity
public class User{
    /**
     * Enumeration for the role of each user. Determines 
     * the permissions that this user has.
     */
    public enum Role{
        USER,
        CREATOR,
        ADMIN,
        DEV
    } 
    public enum Mode{
        LIGHT,
        DARK
    }
    public enum Notifications{
        ON,
        OFF
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
  
    @Column(name="username")
    String username;

    @Column(name="email")
    String email;

    @Column(name="password")
    String password;

    @Column(name="role")
    Role role;

    @Column(name="mode")
    Mode mode;

    @Column(name="notifications")
    Notifications notifications;

    public Long getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }
    public Role getRole(){
        return role;
    }
    public Mode getMode(){
        return mode;
    }
    public Notifications getNotifications(){
        return notifications;
    }
    public void setUsername(String name){
        username = name;
    }
    public void setEmail(String newEmail){
        email = newEmail;
    }
    public void setPassword(String newPass){
        password = newPass;
    }
    public void setRole(Role newRole){
        role = newRole;
    }
    public void setMode(Mode newMode){
        mode = newMode;
    }
    public void setNotifications(Notifications newNotifications){
        notifications = newNotifications;
    }
    
    /**
     * Preforms a check to see if the user that created the current request 
     * has the required role or higher.
     * 
     * @param requiredRole  The minimum role needed to preform this action
     * @return              True if the user has access, false if they dont
     */
    public static boolean checkPermission(Role requiredRole){
        CarbonUserPrincipal auth =(CarbonUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //check all permissions including and above required role
        for(Role r : Role.values()){
            //skip lower roles
            if(r.compareTo(requiredRole) < 0 ){
                continue;
            }

            if(auth.getAuthorities().stream().anyMatch(a ->
            a.getAuthority().equals(r.toString()))){
                
                // user has at least the minimum role
                return true;
            }
        }
        //user does not have at least the minimum role
        return false;
    }

    /**
     * Copies the details from the other user into this user.
     * DOES NOT COPY PASSWORD!
     * DOES NOT COPY ID!
     * Skips null variables effectively updating this user with the 
     * changes from the other user.
     * 
     * @param other The other user to copy from
     */
    public void copyFrom(User other){
        if(other.username != null){
            this.username = other.username;
        }
        if(other.email != null){
            this.email = other.email;
        }
        if(other.role != null){
            this.role = other.role;
        }
    }
}
