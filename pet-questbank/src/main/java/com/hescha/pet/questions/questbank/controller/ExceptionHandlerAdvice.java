package com.hescha.pet.questions.questbank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Void> dispatchNotFound(NoSuchElementException exception) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
