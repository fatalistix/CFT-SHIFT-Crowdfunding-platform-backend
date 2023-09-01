package ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions;

import java.util.UUID;

public class ProjectNotFoundException extends Exception {
    public ProjectNotFoundException(String projectName) {
        super("Project with name '" + projectName + "' does not exists");
    }

    public ProjectNotFoundException(UUID uuid) {
        super("Project with UUID " + uuid + " does not exists");
    }
}
