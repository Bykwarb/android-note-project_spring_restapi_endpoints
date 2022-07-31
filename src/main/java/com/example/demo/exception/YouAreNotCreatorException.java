package com.example.demo.exception;

public class YouAreNotCreatorException extends RuntimeException{
    public YouAreNotCreatorException(String message){
        super(message);
    }
}
