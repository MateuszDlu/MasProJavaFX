package com.maspro.masprojavafx.Exceptions;

public class InvalidInputException extends Exception { // simple custom exception for when invalid input is provided
    public InvalidInputException(String errorMessage){
        super(errorMessage);
    }
}
