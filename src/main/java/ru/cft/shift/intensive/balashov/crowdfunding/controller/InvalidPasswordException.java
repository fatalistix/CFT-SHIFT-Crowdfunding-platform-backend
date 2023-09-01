package ru.cft.shift.intensive.balashov.crowdfunding.controller;

public class InvalidPasswordException extends Exception {
    public InvalidPasswordException() {
        super("Incorrect old password");
    }
}
