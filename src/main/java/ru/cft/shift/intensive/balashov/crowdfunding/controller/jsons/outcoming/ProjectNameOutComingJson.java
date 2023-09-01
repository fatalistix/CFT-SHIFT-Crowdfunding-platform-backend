package ru.cft.shift.intensive.balashov.crowdfunding.controller.jsons.outcoming;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProjectNameOutComingJson(@JsonProperty("project_name") String projectName) {
}
