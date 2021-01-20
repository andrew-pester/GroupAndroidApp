package com.tc_4.carbon_counter.exceptions;

public class UnauthorizedException extends RuntimeException{
    
    public UnauthorizedException(String msg){
        super(msg);
    }
}
