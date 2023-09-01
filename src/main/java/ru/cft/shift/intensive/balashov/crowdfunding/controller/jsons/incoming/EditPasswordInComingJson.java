package ru.cft.shift.intensive.balashov.crowdfunding.controller.jsons.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EditPasswordInComingJson(@JsonProperty("old_password") String oldPassword,
                                       @JsonProperty("new_password") String newPassword) {
}
