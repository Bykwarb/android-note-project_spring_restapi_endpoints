package com.example.demo.exception;

public class UserIsCreatorException extends RuntimeException{
    public UserIsCreatorException(String message){
        super(message);
    }
}
