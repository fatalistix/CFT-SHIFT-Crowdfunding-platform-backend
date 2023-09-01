package ru.cft.shift.intensive.balashov.crowdfunding.service.impl;

import org.springframework.stereotype.Service;
import ru.cft.shift.intensive.balashov.crowdfunding.enums.ProjectCategory;
import ru.cft.shift.intensive.balashov.crowdfunding.repositories.projects.*;
import ru.cft.shift.intensive.balashov.crowdfunding.service.ProjectsHybridRepository;
import ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions.AnotherProjectExistsException;
import ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions.ProjectNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectsHybridRepositoryImpl implements ProjectsHybridRepository {
    private final CassandraProjectsRepository projectsRepository;
    private final CassandraProjectNameToUUIDRepository projectNameToUUIDRepository;
    private final CassandraProjectCategoryToUUIDSet projectCategoryToUUIDSet;

    public ProjectsHybridRepositoryImpl(CassandraProjectsRepository projectsRepository, CassandraProjectNameToUUIDRepository projectNameToUUIDRepository, CassandraProjectCategoryToUUIDSet projectCategoryToUUIDSet) {
        this.projectsRepository = projectsRepository;
        this.projectNameToUUIDRepository = projectNameToUUIDRepository;
        this.projectCategoryToUUIDSet = projectCategoryToUUIDSet;
    }


    @Override
    public UUID createProject(String author, String projectName, String description, Long requiredAmount, LocalDate donationDeadLine, String videoLink, ProjectCategory category) throws AnotherProjectExistsException {
        if (projectNameToUUIDRepository.existsById(projectName)) {
            throw new AnotherProjectExistsException(projectName);
        }
        UUID projectUuid = UUID.randomUUID();
        while (projectsRepository.existsById(projectUuid)) {
            projectUuid = UUID.randomUUID();
        }
        ProjectRepositoryEntity projectRepositoryEntity = new ProjectRepositoryEntity(projectUuid, author, projectName, description, requiredAmount, donationDeadLine, videoLink, category);
        ProjectNameToUUIDEntity projectNameToUUIDEntity = new ProjectNameToUUIDEntity(projectName, projectUuid);

        projectsRepository.insert(projectRepositoryEntity);
        projectNameToUUIDRepository.insert(projectNameToUUIDEntity);

        ProjectNameTo

        return projectUuid;
    }

    @Override
    public ProjectRepositoryEntity getProject(String name) throws ProjectNotFoundException {
        return projectsRepository.findById(projectNameToUUIDRepository.findById(name).orElseThrow(() -> new ProjectNotFoundException(name)).uuid()).orElseThrow(() -> new ProjectNotFoundException(name));
    }

    @Override
    public ProjectRepositoryEntity getProject(UUID uuid) throws ProjectNotFoundException {
        return projectsRepository.findById(uuid).orElseThrow(() -> new ProjectNotFoundException(uuid));
    }

    @Override
    public List<ProjectRepositoryEntity> getAllProjects() {
        return projectsRepository.findAll();
    }

    @Override
    public List<ProjectRepositoryEntity> getProjectsByCategories(ProjectCategory[] categories) {

    }

    @Override
    public List<ProjectRepositoryEntity> getProjectsByNames(Iterable<String> names) {
        return projectsRepository.findAllById(projectNameToUUIDRepository.findAllById(names).stream().map(ProjectNameToUUIDEntity::uuid).toList());
    }

    @Override
    public List<ProjectRepositoryEntity> getProjectsByIds(Iterable<UUID> ids) {
        return projectsRepository.findAllById(ids);
    }

    @Override
    public void editProject(UUID projectUuid, String projectName, String description, Long requiredAmount, LocalDate donationDeadLine, String videoLink, ProjectCategory category) throws ProjectNotFoundException, AnotherProjectExistsException {
        ProjectRepositoryEntity entity = projectsRepository.findById(projectUuid).orElseThrow(() -> new ProjectNotFoundException(projectUuid));
        if (projectName.equals(entity.projectName())) {
            ProjectRepositoryEntity newEntity = new ProjectRepositoryEntity(entity.id(), entity.author(), entity.projectName(), description, requiredAmount, donationDeadLine, videoLink, entity.category());
            projectsRepository.insert(newEntity);
        } else {
            if (projectNameToUUIDRepository.existsById(projectName)) {
                throw new AnotherProjectExistsException(projectName);
            }
            ProjectRepositoryEntity newEntity = new ProjectRepositoryEntity(entity.id(), entity.author(), projectName, description, requiredAmount, donationDeadLine, videoLink, category);
            projectsRepository.insert(newEntity);

            ProjectNameToUUIDEntity nameToUUIDEntity = projectNameToUUIDRepository.findById(projectName).orElseThrow(() -> new ProjectNotFoundException(projectName));
            projectNameToUUIDRepository.delete(nameToUUIDEntity);
            nameToUUIDEntity = new ProjectNameToUUIDEntity(projectName, nameToUUIDEntity.uuid());
            projectNameToUUIDRepository.insert(nameToUUIDEntity);
        }
    }

    @Override
    public void addMoney(UUID projectUuid, String donationAuthor, Long amount) throws ProjectNotFoundException {
        ProjectRepositoryEntity entity = projectsRepository.findById(projectUuid).orElseThrow(() -> new ProjectNotFoundException(projectUuid));
        entity.transactions().add(new Transaction(donationAuthor, entity.projectName(), amount));
        entity = new ProjectRepositoryEntity(entity.id(), entity.author(), entity.projectName(), entity.description(), entity.requiredAmount(), entity.collectedAmount() + amount, entity.donationDeadline(), entity.videoLink(), entity.transactions(), entity.category());
        projectsRepository.insert(entity);
    }

    @Override
    public UUID getByName(String name) throws ProjectNotFoundException {
        return null;
    }
}
