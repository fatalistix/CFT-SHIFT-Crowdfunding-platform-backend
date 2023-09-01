package ru.cft.shift.intensive.balashov.crowdfunding.service;

import ru.cft.shift.intensive.balashov.crowdfunding.enums.ProjectCategory;
import ru.cft.shift.intensive.balashov.crowdfunding.service.dto.ExtraUserDto;
import ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions.*;

import java.time.LocalDate;
import java.util.UUID;

public interface UserService extends BasicService {
    ExtraUserDto getMyself(String login) throws UserNotFoundException;
    void editUserInfo(String login, String newFirstName, String newLastName, String newPatronymic, LocalDate newBirthDate, String newAbout) throws UserNotFoundException;
    void editUserLogin(String oldLogin, String newLogin) throws UserNotFoundException, AnotherUserExistsException;
    void editUserPassword(String login, String newPassword) throws UserNotFoundException;
    void createNewProject(String login, String projectName, String description, Long requiredAmount, LocalDate donationDeadLine, String videoLink, ProjectCategory category) throws UserNotFoundException, AnotherProjectExistsException;
    void donate(String login, UUID uuid, long money) throws NotEnoughMoneyException, UserNotFoundException, ProjectNotFoundException;
    long activatePromo(String login, String promo) throws UserNotFoundException, PromoNotFoundException;
    boolean passwordMatches(String login, String password);
}
