package com.tc_4.carbon_counter.models;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    long id;

    //username to receive notification
    @Column(name="username")
    String username;

    @Column(name="is_read")
    boolean isRead;

    @Column(name="message")
    String message;


    public long getId(){
        return id;
    }
    public String getUsername(){
        return username;
    }
    public boolean isRead(){
        return isRead;
    }
    public String getMessage(){
        return message;
    }

    public void setUsername(String username){
        this.username = username;
    } 
    public void setIsRead(boolean isRead){
        this.isRead = isRead;
    }
    public void setMessage(String message){
        this.message = message;
    }    
}
