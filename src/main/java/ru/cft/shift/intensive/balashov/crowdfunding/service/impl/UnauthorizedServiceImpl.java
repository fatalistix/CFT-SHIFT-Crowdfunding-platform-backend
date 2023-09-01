package ru.cft.shift.intensive.balashov.crowdfunding.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.cft.shift.intensive.balashov.crowdfunding.repositories.users.CassandraUsersRepository;
import ru.cft.shift.intensive.balashov.crowdfunding.repositories.users.UserRepositoryEntity;
import ru.cft.shift.intensive.balashov.crowdfunding.enums.UserRole;
import ru.cft.shift.intensive.balashov.crowdfunding.service.ProjectsHybridRepository;
import ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions.AnotherUserExistsException;
import ru.cft.shift.intensive.balashov.crowdfunding.service.UnauthorizedService;

import java.time.LocalDate;

@Service
public class UnauthorizedServiceImpl extends BasicServiceImpl implements UnauthorizedService {
    public UnauthorizedServiceImpl(CassandraUsersRepository cassandraUsersRepository, ProjectsHybridRepository projectsHybridRepository, PasswordEncoder passwordEncoder) {
        super(cassandraUsersRepository, projectsHybridRepository, passwordEncoder);
    }

    @Override
    public void createNewUser(String login, String password, String firstName, String lastName, String patronymic,
                              LocalDate birthDate, String about) throws AnotherUserExistsException {
        if (this.userExists(login)) {
            throw new AnotherUserExistsException(login);
        }
        usersRepository.insert(new UserRepositoryEntity(login, UserRole.USER, this.passwordEncoder.encode(password), firstName, lastName,
                patronymic, about, 0L, null,
                birthDate));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserRepositoryEntity entity = usersRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("User with login '" + username + "' not found"));
        return User.builder()
                .username(entity.login())
                .password(entity.password())
                .roles(entity.role().name())
                .build();
    }
}



//    public ExtraUserDto getUserExtraData(String login) throws UserNotFoundException {
//        UserRepositoryEntity entity = usersRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
//        return new ExtraUserDto(entity.login(), entity.firstName(), entity.lastName(), entity.patronymic(), entity.about(), entity.birthDate(), entity.money());
//    }

//    @Override
//    public long activatePromo(String login, String promo) throws PromoNotFoundException, UserNotFoundException {
//        PromoRepositoryEntity promoEntity = promosRepository.findById(promo).orElseThrow(() -> new PromoNotFoundException(promo));
//        promosRepository.delete(promoEntity);
//        UserRepositoryEntity userEntity = usersRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
//        //TODO: CONVERT TO CLASS AND REMOVE CREATING NEW RECORD
//        usersRepository.insert(new UserRepositoryEntity(userEntity.login(),
//                userEntity.role(),
//                userEntity.password(),
//                userEntity.firstName(),
//                userEntity.lastName(),
//                userEntity.patronymic(),
//                userEntity.about(),
//                userEntity.money() + promoEntity.money(),
//                userEntity.projectsId(),
//                userEntity.birthDate()));
//        return promoEntity.money();
//    }

//    @Override
//    public void editUserInfo(String login, String firstName, String lastName, String patronymic, String about, LocalDate birthDate) throws UserNotFoundException {
//        UserRepositoryEntity oldEntity = usersRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
//        //TODO: SAME CONVERT FROM RECORD TO CLASS TO AVOID CONSTRUCTING NEW OBJECT
//        UserRepositoryEntity newEntity = new UserRepositoryEntity(oldEntity.login(), oldEntity.role(), oldEntity.password(), firstName, lastName, patronymic, about, oldEntity.money(), oldEntity.projectsId(), birthDate);
//        //TODO: Think about usersRepository.save()
//        usersRepository.insert(newEntity);
//    }




//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.SecurityFilterChain;


//        SecurityContextHolder.getContext().setAuthentication();
//        AuthenticationManager
//
//
//        SecurityFilterChain


// еще таблицу для каждой категории сортировки, чтоб было оптимизированно
// synchronized on modification
