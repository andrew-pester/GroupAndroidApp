package com.tc_4.carbon_counter.models;

import javax.persistence.*;

@Entity
public class Friends {

    public enum Status{
        REQUESTED,
        APPROVED,
        DENIED
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int id;
  
    @Column(name="user_one")
    String userOne;

    @Column(name="user_two")
    String userTwo;

    @Column(name="status")
    Status status;

    public Friends(){
        super();
    }

    public long getId(){
        return id;
    }
    public String getUserOne(){
        return userOne;
    }
    public String getUserTwo(){
        return userTwo;
    }
    public Status getStatus(){
        return status;
    }
    public void setUserOne(String username){
        userOne = username;
    }
    public void setUserTwo(String username){
        userTwo = username;
    }
    public void setStatus(Status status){
        this.status = status;
    }

    

}
