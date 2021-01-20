package com.tc_4.carbon_counter.exceptions;

public class TitleTakenException extends RuntimeException {
    public TitleTakenException(String title){
        super("The title '" + title + "' is taken or is in review.");
    }
}
