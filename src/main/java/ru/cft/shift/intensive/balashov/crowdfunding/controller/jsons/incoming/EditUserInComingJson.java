package ru.cft.shift.intensive.balashov.crowdfunding.controller.jsons.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EditUserInComingJson(@JsonProperty("first_name") String firstName,
                                   @JsonProperty("last_name") String lastName,
                                   String patronymic,
                                   String about,
                                   @JsonProperty("birth_date") String birthDate) {
}
