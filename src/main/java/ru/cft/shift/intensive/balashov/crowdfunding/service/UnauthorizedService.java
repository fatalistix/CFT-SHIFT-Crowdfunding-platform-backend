package ru.cft.shift.intensive.balashov.crowdfunding.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions.AnotherUserExistsException;

import java.time.LocalDate;

//? Common interface and every user service extends it?
public interface UnauthorizedService extends UserDetailsService, BasicService {
    void createNewUser(String login, String password, String firstName, String lastName, String patronymic, LocalDate birthDate, String about) throws AnotherUserExistsException;
}
