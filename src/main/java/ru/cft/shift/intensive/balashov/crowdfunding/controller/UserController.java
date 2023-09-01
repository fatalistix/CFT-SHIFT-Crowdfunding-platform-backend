package ru.cft.shift.intensive.balashov.crowdfunding.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.cft.shift.intensive.balashov.crowdfunding.controller.jsons.incoming.*;
import ru.cft.shift.intensive.balashov.crowdfunding.controller.jsons.outcoming.AmountOutComingJson;
import ru.cft.shift.intensive.balashov.crowdfunding.controller.jsons.outcoming.AuthStatusOutComingJson;
import ru.cft.shift.intensive.balashov.crowdfunding.controller.jsons.outcoming.LoginOutComingJson;
import ru.cft.shift.intensive.balashov.crowdfunding.controller.jsons.outcoming.ProjectNameOutComingJson;
import ru.cft.shift.intensive.balashov.crowdfunding.enums.ProjectCategory;
import ru.cft.shift.intensive.balashov.crowdfunding.service.UserService;
import ru.cft.shift.intensive.balashov.crowdfunding.service.dto.BasicProjectDto;
import ru.cft.shift.intensive.balashov.crowdfunding.service.dto.BasicUserDto;
import ru.cft.shift.intensive.balashov.crowdfunding.service.dto.ExtraUserDto;
import ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
@PropertySource("classpath:/api.properties")
@ResponseBody
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("${api.user.users.myself}")
    public ResponseEntity<ExtraUserDto> getMyself(Principal principal) throws UserNotFoundException {
        return ResponseEntity.ok(service.getMyself(principal.getName()));
    }

    @GetMapping("${api.user.users.get_all}")
    public ResponseEntity<List<BasicUserDto>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsersBasicData());
    }

    @GetMapping("${api.user.users.get}")
    public ResponseEntity<BasicUserDto> getUser(@PathVariable String login) throws UserNotFoundException {
        return ResponseEntity.ok(service.getUserBasicData(login));
    }

    @PutMapping("${api.user.users.edit_info}")
    public ResponseEntity<LoginOutComingJson> editInfo(Principal principal, EditUserInComingJson editData) throws UserNotFoundException {
        service.editUserInfo(principal.getName(), editData.firstName(), editData.lastName(), editData.patronymic(), LocalDate.parse(editData.birthDate(), DateTimeFormatter.ISO_LOCAL_DATE), editData.about());
        return ResponseEntity.ok(new LoginOutComingJson(principal.getName()));
    }

    @PutMapping("${api.user.users.edit_login}")
    public ResponseEntity<LoginOutComingJson> editLogin(Principal principal, ChangeLoginInComingJson changeLoginJson) throws UserNotFoundException, AnotherUserExistsException {
        service.editUserLogin(principal.getName(), changeLoginJson.newLogin());
        return ResponseEntity.ok(new LoginOutComingJson(changeLoginJson.newLogin()));
    }

    @PutMapping("${api.user.users.edit_password}")
    public ResponseEntity<LoginOutComingJson> editPassword(Principal principal, EditPasswordInComingJson passwordJson) throws UserNotFoundException, InvalidPasswordException {
        if (service.passwordMatches(principal.getName(), passwordJson.oldPassword())) {
            service.editUserPassword(principal.getName(), passwordJson.newPassword());
            return ResponseEntity.ok(new LoginOutComingJson(principal.getName()));
        } else {
            throw new InvalidPasswordException();
        }
    }

    @GetMapping("${api.user.projects.get_all}")
    public ResponseEntity<List<BasicProjectDto>> getAllProjects(@RequestParam(value = "enable_sorting", defaultValue = "false") boolean enableSorting,
                                                                @RequestParam(value = "is_ascending", defaultValue = "true") boolean isAscending,
                                                                @RequestParam(defaultValue = "GAMES, SCIENCE, BUSINESS, MOVIE, CHARITY") ProjectCategory[] categories,
                                                                @RequestParam(defaultValue = "DEADLINE_DATE") SortingType type,
                                                                @RequestParam(value = "enable_filtering", defaultValue = "false") boolean enableFiltering,
                                                                @RequestParam(value = "start_index", defaultValue = "0") long startIndex,
                                                                @RequestParam(value = "end_index", defaultValue = "10") long endIndex) {

        return ResponseEntity.ok(service.getAllProjectsBasicData());
    }

    @GetMapping("${api.user.projects.get_by_name}")
    public ResponseEntity<BasicProjectDto> getProjectByName(@PathVariable("project_name") String projectName) throws UserNotFoundException, ProjectNotFoundException {
        return ResponseEntity.ok(service.getProjectByName(projectName));
    }

    @GetMapping("${api.user.projects.get_by_uuid}")
    public ResponseEntity<BasicProjectDto> getProjectByUuid(@PathVariable UUID uuid) throws UserNotFoundException, ProjectNotFoundException {
        return ResponseEntity.ok(service.getProjectByUuid(uuid));
    }

    @GetMapping("${api.user.projects.user.get_all}")
    public ResponseEntity<List<BasicProjectDto>> getAllUserProjects(@PathVariable String login) throws UserNotFoundException {
        return ResponseEntity.ok(service.getAllUsersProjectsBasicData(login));
    }

    @PostMapping("${api.user.projects.create}")
    public ResponseEntity<ProjectNameOutComingJson> createNewProject(Principal principal, CreateProjectInComingJson createProjectJson) throws UserNotFoundException, AnotherProjectExistsException {
        service.createNewProject(principal.getName(), createProjectJson.projectName(), createProjectJson.description(), createProjectJson.requiredAmount(), LocalDate.parse(createProjectJson.donationDeadline(), DateTimeFormatter.ISO_LOCAL_DATE), createProjectJson.videoLink(), createProjectJson.category());
        return ResponseEntity.ok(new ProjectNameOutComingJson(createProjectJson.projectName()));
    }

    @PostMapping("${api.user.projects.donate}")
    public ResponseEntity<AmountOutComingJson> donate(@PathVariable("project_uuid") UUID projectUuid, DonateInComingJson donateJson, Principal principal) throws UserNotFoundException, ProjectNotFoundException, NotEnoughMoneyException {
        service.donate(principal.getName(), projectUuid, donateJson.amount());
        return ResponseEntity.ok(new AmountOutComingJson(donateJson.amount()));
    }

    @PostMapping("${api.user.promos.activate}")
    public ResponseEntity<AmountOutComingJson> activatePromo(Principal principal, PromoInComingJson inComingJson) throws UserNotFoundException, PromoNotFoundException {
        return ResponseEntity.ok(new AmountOutComingJson(service.activatePromo(principal.getName(), inComingJson.promo())));
    }

    @PostMapping("${api.user.users.logout}")
    public ResponseEntity<AuthStatusOutComingJson> logoutMock() {
        return ResponseEntity.ok(new AuthStatusOutComingJson("MOCK"));
    }

    @PostMapping("${api.user.users.logout.success}")
    public ResponseEntity<AuthStatusOutComingJson> logoutSuccessful() {
        return ResponseEntity.ok(new AuthStatusOutComingJson("OK"));
    }
}
