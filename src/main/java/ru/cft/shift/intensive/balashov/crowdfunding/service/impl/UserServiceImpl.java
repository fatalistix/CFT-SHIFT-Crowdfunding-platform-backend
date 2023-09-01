package ru.cft.shift.intensive.balashov.crowdfunding.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.cft.shift.intensive.balashov.crowdfunding.enums.ProjectCategory;
import ru.cft.shift.intensive.balashov.crowdfunding.repositories.promos.CassandraPromosRepository;
import ru.cft.shift.intensive.balashov.crowdfunding.repositories.promos.PromoRepositoryEntity;
import ru.cft.shift.intensive.balashov.crowdfunding.repositories.users.CassandraUsersRepository;
import ru.cft.shift.intensive.balashov.crowdfunding.repositories.users.UserRepositoryEntity;
import ru.cft.shift.intensive.balashov.crowdfunding.service.ProjectsHybridRepository;
import ru.cft.shift.intensive.balashov.crowdfunding.service.UserService;
import ru.cft.shift.intensive.balashov.crowdfunding.service.dto.ExtraUserDto;
import ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions.*;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class UserServiceImpl extends BasicServiceImpl implements UserService {
    private final CassandraPromosRepository promosRepository;
    public UserServiceImpl(CassandraUsersRepository cassandraUsersRepository, ProjectsHybridRepository projectsHybridRepository, CassandraPromosRepository promosRepository, PasswordEncoder passwordEncoder) {
        super(cassandraUsersRepository, projectsHybridRepository, passwordEncoder);
        this.promosRepository = promosRepository;
    }
    @Override
    public ExtraUserDto getMyself(String login) throws UserNotFoundException {
        UserRepositoryEntity entity = usersRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        return new ExtraUserDto(entity.login(),
                entity.firstName(),
                entity.lastName(),
                entity.patronymic(),
                entity.about(),
                entity.birthDate(),
                entity.money());
    }

    @Override
    public void editUserInfo(String login, String newFirstName, String newLastName, String newPatronymic, LocalDate newBirthDate, String newAbout) throws UserNotFoundException {
        UserRepositoryEntity entity = usersRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        entity = new UserRepositoryEntity(entity.login(),
                entity.role(),
                entity.password(),
                newFirstName,
                newLastName,
                newPatronymic,
                newAbout,
                entity.money(),
                entity.projectsId(),
                newBirthDate);
        usersRepository.insert(entity);
    }

    @Override
    public void editUserLogin(String oldLogin, String newLogin) throws UserNotFoundException, AnotherUserExistsException {
        if (userExists(newLogin)) {
            throw new AnotherUserExistsException(newLogin);
        }
        UserRepositoryEntity entity = usersRepository.findById(oldLogin).orElseThrow(() -> new UserNotFoundException(oldLogin));
        usersRepository.delete(entity);
        entity = new UserRepositoryEntity(newLogin, entity.role(), entity.password(), entity.firstName(), entity.lastName(), entity.patronymic(), entity.about(), entity.money(), entity.projectsId(), entity.birthDate());
        usersRepository.insert(entity);
    }

    @Override
    public void editUserPassword(String login, String newPassword) throws UserNotFoundException {
        UserRepositoryEntity entity = usersRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        entity = new UserRepositoryEntity(entity.login(), entity.role(), newPassword, entity.firstName(), entity.lastName(), entity.patronymic(), entity.about(), entity.money(), entity.projectsId(), entity.birthDate());
        usersRepository.insert(entity);
    }

    @Override
    public void createNewProject(String login, String projectName, String description, Long requiredAmount, LocalDate donationDeadLine, String videoLink, ProjectCategory category) throws UserNotFoundException,  AnotherProjectExistsException {
        UserRepositoryEntity userRepositoryEntity = usersRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        UUID uuid = projectsHybridRepository.createProject(login, projectName, description, requiredAmount, donationDeadLine, videoLink, category);
        userRepositoryEntity.projectsId().add(uuid);
        usersRepository.insert(userRepositoryEntity);
    }

    @Override
    public void donate(String login, UUID uuid, long money) throws NotEnoughMoneyException, UserNotFoundException, ProjectNotFoundException {
        UserRepositoryEntity userEntity = usersRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        long newMoney;
        if (userEntity.money() < money) {
            throw new NotEnoughMoneyException(userEntity.money(), money);
        } else {
            newMoney = userEntity.money() - money;
        }

        userEntity = new UserRepositoryEntity(userEntity.login(), userEntity.role(), userEntity.password(), userEntity.firstName(), userEntity.lastName(), userEntity.patronymic(), userEntity.about(), newMoney, userEntity.projectsId(), userEntity.birthDate());
        usersRepository.insert(userEntity);
        projectsHybridRepository.addMoney(uuid, login, money);
    }

    @Override
    public long activatePromo(String login, String promo) throws UserNotFoundException, PromoNotFoundException {
        PromoRepositoryEntity promoEntity = promosRepository.findById(promo).orElseThrow(() -> new PromoNotFoundException(promo));
        UserRepositoryEntity  userEntity  = usersRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        userEntity = new UserRepositoryEntity(userEntity.login(),
                userEntity.role(),
                userEntity.password(),
                userEntity.firstName(),
                userEntity.lastName(),
                userEntity.patronymic(),
                userEntity.about(),
                userEntity.money() + promoEntity.money(),
                userEntity.projectsId(),
                userEntity.birthDate());
        promosRepository.delete(promoEntity);
        usersRepository.insert(userEntity);
        return promoEntity.money();
    }

    @Override
    public boolean passwordMatches(String login, String password) {
        return usersRepository.existsByLoginAndPassword(login, passwordEncoder.encode(password));
    }
}
