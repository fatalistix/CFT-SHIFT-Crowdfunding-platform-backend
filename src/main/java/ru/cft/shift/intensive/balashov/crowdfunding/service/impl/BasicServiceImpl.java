package ru.cft.shift.intensive.balashov.crowdfunding.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.cft.shift.intensive.balashov.crowdfunding.controller.SortingType;
import ru.cft.shift.intensive.balashov.crowdfunding.enums.ProjectCategory;
import ru.cft.shift.intensive.balashov.crowdfunding.repositories.projects.ProjectRepositoryEntity;
import ru.cft.shift.intensive.balashov.crowdfunding.repositories.users.CassandraUsersRepository;
import ru.cft.shift.intensive.balashov.crowdfunding.repositories.users.UserRepositoryEntity;
import ru.cft.shift.intensive.balashov.crowdfunding.service.BasicService;
import ru.cft.shift.intensive.balashov.crowdfunding.service.ProjectsHybridRepository;
import ru.cft.shift.intensive.balashov.crowdfunding.service.dto.BasicProjectDto;
import ru.cft.shift.intensive.balashov.crowdfunding.service.dto.BasicUserDto;
import ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions.ProjectNotFoundException;
import ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BasicServiceImpl implements BasicService {
    protected final CassandraUsersRepository usersRepository;
    protected final ProjectsHybridRepository projectsHybridRepository;
    protected final PasswordEncoder passwordEncoder;

    @Autowired
    public BasicServiceImpl(CassandraUsersRepository cassandraUsersRepository, ProjectsHybridRepository projectsHybridRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = cassandraUsersRepository;
        this.projectsHybridRepository = projectsHybridRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean userExists(String login) {
        return usersRepository.existsById(login);
    }

    @Override
    public List<BasicUserDto> getAllUsersBasicData() {
        return usersRepository.findAll().stream().map(entity -> new BasicUserDto(entity.firstName(), entity.lastName(), entity.patronymic(), entity.about(), entity.birthDate())).toList();
    }

    @Override
    public BasicUserDto getUserBasicData(String login) throws UserNotFoundException {
        UserRepositoryEntity entity = usersRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        return new BasicUserDto(entity.firstName(), entity.lastName(), entity.patronymic(), entity.about(), entity.birthDate());
    }

    @Override
    public List<BasicProjectDto> getAllProjectsBasicData() {
        return projectsHybridRepository.getAllProjects().stream().map(projectEntity -> {
            UserRepositoryEntity userEntity = usersRepository.findById(projectEntity.author()).orElse(null);
            if (userEntity == null) {
                return null;
            }
            return new BasicProjectDto(projectEntity.id(),
                    projectEntity.projectName(),
                    new BasicUserDto(userEntity.firstName(),
                            userEntity.lastName(),
                            userEntity.patronymic(),
                            userEntity.about(),
                            userEntity.birthDate()),
                    projectEntity.requiredAmount(),
                    projectEntity.collectedAmount(),
                    projectEntity.donationDeadline(),
                    projectEntity.description(),
                    projectEntity.videoLink());
        }).filter(Objects::isNull).toList();
    }

    @Override
    public List<BasicProjectDto> getFilteredProjectsBasicData(boolean ascending, ProjectCategory[] categories, SortingType type) {
        return null;
    }

    @Override
    public BasicProjectDto getProjectByName(String name) throws UserNotFoundException, ProjectNotFoundException {
        ProjectRepositoryEntity projectEntity = projectsHybridRepository.getProject(name);
        UserRepositoryEntity userEntity = usersRepository.findById(projectEntity.author()).orElseThrow(() -> new UserNotFoundException(projectEntity.author()));
        return new BasicProjectDto(projectEntity.id(),
                projectEntity.projectName(),
                new BasicUserDto(userEntity.firstName(),
                        userEntity.lastName(),
                        userEntity.patronymic(),
                        userEntity.about(),
                        userEntity.birthDate()),
                projectEntity.requiredAmount(),
                projectEntity.collectedAmount(),
                projectEntity.donationDeadline(),
                projectEntity.description(),
                projectEntity.videoLink());
    }

    @Override
    public BasicProjectDto getProjectByUuid(UUID uuid) throws ProjectNotFoundException, UserNotFoundException {
        ProjectRepositoryEntity projectEntity = projectsHybridRepository.getProject(uuid);
        UserRepositoryEntity userEntity = usersRepository.findById(projectEntity.author()).orElseThrow(() -> new UserNotFoundException(projectEntity.author()));
        return new BasicProjectDto(projectEntity.id(),
                projectEntity.projectName(),
                new BasicUserDto(userEntity.firstName(),
                        userEntity.lastName(),
                        userEntity.patronymic(),
                        userEntity.about(),
                        userEntity.birthDate()),
                projectEntity.requiredAmount(),
                projectEntity.collectedAmount(),
                projectEntity.donationDeadline(),
                projectEntity.description(),
                projectEntity.videoLink());
    }

    @Override
    public List<BasicProjectDto> getAllUsersProjectsBasicData(String user) throws UserNotFoundException {
        UserRepositoryEntity userEntity = usersRepository.findById(user).orElseThrow(() -> new UserNotFoundException(user));
        BasicUserDto basicUserDto = new BasicUserDto(userEntity.firstName(), userEntity.lastName(), userEntity.patronymic(), userEntity.about(), userEntity.birthDate());
        return projectsHybridRepository.getProjectsByIds(userEntity.projectsId())
                .stream()
                .map(projectRepositoryEntity ->
                        new BasicProjectDto(projectRepositoryEntity.id(),
                                projectRepositoryEntity.projectName(),
                                basicUserDto,
                                projectRepositoryEntity.requiredAmount(),
                                projectRepositoryEntity.collectedAmount(),
                                projectRepositoryEntity.donationDeadline(),
                                projectRepositoryEntity.description(),
                                projectRepositoryEntity.videoLink())
                ).toList();
    }
}
