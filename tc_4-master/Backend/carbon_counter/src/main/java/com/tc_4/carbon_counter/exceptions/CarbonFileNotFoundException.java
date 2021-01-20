package com.tc_4.carbon_counter.exceptions;

public class CarbonFileNotFoundException extends RuntimeException{
    public CarbonFileNotFoundException(String fileName){
        super("Could not find file: " + fileName);
        
    }
}
