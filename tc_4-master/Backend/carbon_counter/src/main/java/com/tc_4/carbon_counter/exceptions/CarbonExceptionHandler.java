package com.tc_4.carbon_counter.exceptions;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CarbonExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Map<String, String> handleUserNotFoundException(UserNotFoundException e){
        return Collections.singletonMap("message", e.getMessage());
    }

    @ExceptionHandler(CarbonFileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, String> handleCarbonFileNotFoundException(CarbonFileNotFoundException e){
        return Collections.singletonMap("message", e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Map<String, String> handleUnauthorizedException(UnauthorizedException e){
        return Collections.singletonMap("message", e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public Map<String, String> handleJsonMappingException(HttpMessageNotReadableException e) {
        return Collections.singletonMap("message", e.getMessage());
    }
}