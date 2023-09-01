package ru.cft.shift.intensive.balashov.crowdfunding.service.dto;

import java.time.LocalDate;

public record BasicUserDto(String firstName, String lastName, String patronymic, String about, LocalDate birthDate) {

}
