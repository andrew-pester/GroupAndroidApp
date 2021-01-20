package com.tc_4.carbon_counter.exceptions;

public class UserNotFoundException extends RuntimeException{
    
    public UserNotFoundException(String username){
        super("Could not find user with username: " + username);
        
    }
}
