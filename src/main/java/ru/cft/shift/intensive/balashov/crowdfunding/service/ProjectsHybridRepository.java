package ru.cft.shift.intensive.balashov.crowdfunding.service;

import ru.cft.shift.intensive.balashov.crowdfunding.enums.ProjectCategory;
import ru.cft.shift.intensive.balashov.crowdfunding.repositories.projects.ProjectRepositoryEntity;
import ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions.AnotherProjectExistsException;
import ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions.ProjectNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ProjectsHybridRepository {
    UUID createProject(String author, String projectName, String description, Long requiredAmount, LocalDate donationDeadLine, String videoLink, ProjectCategory category) throws AnotherProjectExistsException;
    ProjectRepositoryEntity getProject(String projectName) throws ProjectNotFoundException;
    ProjectRepositoryEntity getProject(UUID uuid) throws ProjectNotFoundException;
    List<ProjectRepositoryEntity> getAllProjects();
    List<ProjectRepositoryEntity> getProjectsByCategories(ProjectCategory[] categories);
    List<ProjectRepositoryEntity> getProjectsByNames(Iterable<String> names);
    List<ProjectRepositoryEntity> getProjectsByIds(Iterable<UUID> ids);
    void editProject(UUID projectUuid, String projectName, String description, Long requiredAmount, LocalDate donationDeadLine, String videoLink, ProjectCategory category) throws ProjectNotFoundException, AnotherProjectExistsException;
    void addMoney(UUID projectUuid, String donationAuthor, Long amount) throws ProjectNotFoundException;
    UUID getByName(String name) throws ProjectNotFoundException;
}
