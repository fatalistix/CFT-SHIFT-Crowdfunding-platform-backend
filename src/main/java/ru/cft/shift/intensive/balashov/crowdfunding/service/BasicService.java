package ru.cft.shift.intensive.balashov.crowdfunding.service;

import ru.cft.shift.intensive.balashov.crowdfunding.controller.SortingType;
import ru.cft.shift.intensive.balashov.crowdfunding.enums.ProjectCategory;
import ru.cft.shift.intensive.balashov.crowdfunding.service.dto.BasicProjectDto;
import ru.cft.shift.intensive.balashov.crowdfunding.service.dto.BasicUserDto;
import ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions.ProjectNotFoundException;
import ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions.UserNotFoundException;

import java.util.List;
import java.util.UUID;

public interface BasicService {
    boolean userExists(String login);
    List<BasicUserDto> getAllUsersBasicData();
    BasicUserDto getUserBasicData(String login) throws UserNotFoundException;
    List<BasicProjectDto> getAllProjectsBasicData();
    List<BasicProjectDto> getFilteredProjectsBasicData(boolean ascending,
                                                       ProjectCategory[] categories,
                                                       SortingType type);
    BasicProjectDto getProjectByName(String name) throws UserNotFoundException, ProjectNotFoundException;
    BasicProjectDto getProjectByUuid(UUID uuid) throws ProjectNotFoundException, UserNotFoundException;
    List<BasicProjectDto> getAllUsersProjectsBasicData(String user) throws UserNotFoundException;
}
