package ru.cft.shift.intensive.balashov.crowdfunding.service.dto;

import java.time.LocalDate;
import java.util.UUID;

public record BasicProjectDto(UUID uuid, String projectName, BasicUserDto author, Long requiredAmount, Long collectedAmount, LocalDate donationDeadline, String description, String videoWidget) {
}
