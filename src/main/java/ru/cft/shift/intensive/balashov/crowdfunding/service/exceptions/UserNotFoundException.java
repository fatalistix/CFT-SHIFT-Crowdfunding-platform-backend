package ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String login) {
        super("User with login '" + login + "' not found");
    }
}
