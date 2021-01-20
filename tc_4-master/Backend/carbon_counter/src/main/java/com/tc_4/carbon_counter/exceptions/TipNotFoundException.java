package com.tc_4.carbon_counter.exceptions;

public class TipNotFoundException extends RuntimeException {
    public TipNotFoundException(String title){
        super("The title '" + title + "' cannot be found or is in review.");
    }
}
