package ru.cft.shift.intensive.balashov.crowdfunding.repositories.users;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import ru.cft.shift.intensive.balashov.crowdfunding.enums.UserRole;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;


@Table(value = "Users")
public record UserRepositoryEntity(@PrimaryKey String login,
                                   UserRole role,
                                   String password,
                                   String firstName,
                                   String lastName,
                                   String patronymic,
                                   String about,
                                   long money,
                                   Set<UUID> projectsId,
                                   LocalDate birthDate) {}