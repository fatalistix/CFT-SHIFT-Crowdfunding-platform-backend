package ru.cft.shift.intensive.balashov.crowdfunding.controller.jsons.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateUserInComingJson(String login, @JsonProperty("first_name") String firstName,
                                     @JsonProperty("last_name") String lastName, String patronymic,
                                     @JsonProperty("birth_date") String birthDate, String about, String password) {
}
