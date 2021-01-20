package com.tc_4.carbon_counter.exceptions;

public class RequestNotFoundException extends RuntimeException{
    public RequestNotFoundException(String userOne, String userTwo){
        super("The request between "+ userOne +" and " + userTwo + " could not be found.");
    }
}
