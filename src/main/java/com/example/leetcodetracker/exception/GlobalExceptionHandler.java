package com.example.leetcodetracker.exception;

import com.example.leetcodetracker.problem.ProblemAlreadyExistsException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProblemAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleProblemAlreadyExists(ProblemAlreadyExistsException e) {
    }

}
