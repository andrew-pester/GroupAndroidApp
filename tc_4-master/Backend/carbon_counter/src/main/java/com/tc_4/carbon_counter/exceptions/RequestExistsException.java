package com.tc_4.carbon_counter.exceptions;

public class RequestExistsException extends RuntimeException {
    public RequestExistsException(){
        super("This friend request already exists.");
    } 
}
