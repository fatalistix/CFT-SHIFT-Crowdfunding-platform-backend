package ru.cft.shift.intensive.balashov.crowdfunding.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.cft.shift.intensive.balashov.crowdfunding.controller.jsons.incoming.CreateUserInComingJson;
import ru.cft.shift.intensive.balashov.crowdfunding.controller.jsons.outcoming.AuthStatusOutComingJson;
import ru.cft.shift.intensive.balashov.crowdfunding.controller.jsons.outcoming.ErrorMessageOutComingJson;
import ru.cft.shift.intensive.balashov.crowdfunding.controller.jsons.outcoming.LoginOutComingJson;
import ru.cft.shift.intensive.balashov.crowdfunding.service.UnauthorizedService;
import ru.cft.shift.intensive.balashov.crowdfunding.service.dto.BasicProjectDto;
import ru.cft.shift.intensive.balashov.crowdfunding.service.dto.BasicUserDto;
import ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions.ProjectNotFoundException;
import ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions.AnotherUserExistsException;
import ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions.UserNotFoundException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@PropertySource("classpath:/api.properties")
@ResponseBody
@RestController
@Tag(name = "api.docs.unauthorized.tag.name", description = "api.docs.unauthorized.tag.description")
@Validated
public class UnauthorizedController {
    private final UnauthorizedService service;
    private final static Logger log = LoggerFactory.getLogger(UnauthorizedController.class);

    @Autowired
    public UnauthorizedController(UnauthorizedService service) {
        this.service = service;
    }

    @Operation(summary = "api.docs.unauthorized.users.create.summary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "api.docs.unauthorized.users.create.200.description"),
            @ApiResponse(responseCode = "409", description = "api.docs.exception.another-user-exists.description",  content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageOutComingJson.class))}),
            @ApiResponse(responseCode = "500", description = "api.docs.exception.internal-error.description",       content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageOutComingJson.class))})
    })
    @PostMapping("${api.unauthorized.users.create}")
    public ResponseEntity<LoginOutComingJson> createUser(@RequestBody CreateUserInComingJson data) throws AnotherUserExistsException {
        service.createNewUser(data.login(), data.password(), data.firstName(), data.lastName(),
                data.patronymic(),
                LocalDate.parse(data.birthDate(), DateTimeFormatter.ISO_LOCAL_DATE),
                data.about());

        return ResponseEntity.ok(new LoginOutComingJson(data.login()));
    }

    @Operation(summary = "api.unauthorized.users.login.success.summary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "api.docs.unauthorized.users.login.success.200.description"),
            @ApiResponse(responseCode = "500", description = "api.docs.exception.internal-error.description",       content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageOutComingJson.class))})
    })
    @GetMapping("${api.unauthorized.users.login.success}")
    public ResponseEntity<AuthStatusOutComingJson> authorizationSuccessful() {
        return ResponseEntity.ok(new AuthStatusOutComingJson("OK"));
    }

    @GetMapping("${api.unauthorized.users.login.failure}")
    public ResponseEntity<AuthStatusOutComingJson> authorizationFailed() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthStatusOutComingJson("FAIL"));
    }

    @PostMapping("${api.unauthorized.users.login}")
    public ResponseEntity<AuthStatusOutComingJson> authorize() {
        return ResponseEntity.ok(new AuthStatusOutComingJson("LOGIN MOCK"));
    }

    @GetMapping("${api.unauthorized.users.get_all}")
    public ResponseEntity<List<BasicUserDto>> getAllProfiles() {
        return ResponseEntity.ok(service.getAllUsersBasicData());
    }

    @GetMapping("${api.unauthorized.users.get}")
    public ResponseEntity<BasicUserDto> getProfileByLogin(@PathVariable String login) throws UserNotFoundException {
        return ResponseEntity.ok(service.getUserBasicData(login));
    }

    @GetMapping("${api.unauthorized.projects.get_all}")
    public ResponseEntity<List<BasicProjectDto>> getAllProjects() {
        return ResponseEntity.ok(service.getAllProjectsBasicData());
    }

    @GetMapping("${api.unauthorized.projects.get_by_name}")
    public ResponseEntity<BasicProjectDto> getProjectByName(@PathVariable("project_name") String projectName) throws UserNotFoundException, ProjectNotFoundException {
        return ResponseEntity.ok(service.getProjectByName(projectName));
    }

    @GetMapping("${api.unauthorized.projects.get_by_uuid}")
    public ResponseEntity<BasicProjectDto> getProjectByUuid(@PathVariable UUID uuid) throws UserNotFoundException, ProjectNotFoundException {
        return ResponseEntity.ok(service.getProjectByUuid(uuid));
    }

    @GetMapping("${api.unauthorized.projects.user.get_all}")
    public ResponseEntity<List<BasicProjectDto>> getAllUserProjects(@PathVariable String user) throws UserNotFoundException {
        return ResponseEntity.ok(service.getAllUsersProjectsBasicData(user));
    }
}

