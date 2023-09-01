package ru.cft.shift.intensive.balashov.crowdfunding.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.cft.shift.intensive.balashov.crowdfunding.controller.jsons.outcoming.ErrorMessageOutComingJson;
import ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions.AnotherUserExistsException;
import ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions.ProjectNotFoundException;
import ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions.UserNotFoundException;

@RestControllerAdvice
public class ControllersAdvice {
    @ExceptionHandler(AnotherUserExistsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessageOutComingJson userExistsHandler(AnotherUserExistsException e) {
        return new ErrorMessageOutComingJson(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageOutComingJson userNotFoundHandler(UserNotFoundException e) {
        return new ErrorMessageOutComingJson(e.getMessage());
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageOutComingJson projectNotFoundHandler(ProjectNotFoundException e) {
        return new ErrorMessageOutComingJson(e.getMessage());
    }







    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessageOutComingJson uncaughtExceptionHandler(Exception e) {
        return new ErrorMessageOutComingJson("Unexpected exception: " + e.getMessage());
    }
}
