package com.goride.exceptions;

public class UserAlreadyFoundException extends RuntimeException{
    public UserAlreadyFoundException(String message){
        super(message);
    }
}
