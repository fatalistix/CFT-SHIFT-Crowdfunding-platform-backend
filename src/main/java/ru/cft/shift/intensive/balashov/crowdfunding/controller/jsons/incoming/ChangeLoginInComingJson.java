package ru.cft.shift.intensive.balashov.crowdfunding.controller.jsons.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChangeLoginInComingJson(@JsonProperty("new_login") String newLogin) {
}
