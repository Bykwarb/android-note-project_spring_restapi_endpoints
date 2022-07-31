package com.example.demo.exception;

public class UserAlreadyInTheRoomException extends RuntimeException{
    public UserAlreadyInTheRoomException(String message){
        super(message);
    }
}
