package ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions;

public class AnotherProjectExistsException extends Exception {
    public AnotherProjectExistsException(String projectName) {
        super("Project with name '" + projectName + "' exists");
    }
}
