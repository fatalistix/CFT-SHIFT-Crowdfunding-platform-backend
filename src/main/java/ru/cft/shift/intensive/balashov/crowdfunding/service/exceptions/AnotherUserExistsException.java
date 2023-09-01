package ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions;

public class AnotherUserExistsException extends Exception {
    public AnotherUserExistsException(String login) {
        super("User with login '" + login + "' already exists");
    }
}
