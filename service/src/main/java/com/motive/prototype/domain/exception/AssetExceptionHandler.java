package com.motive.prototype.domain.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;


@Slf4j
@ControllerAdvice
public class AssetExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AssetException.class)
    private ResponseEntity<Object> handle(AssetException e, WebRequest request) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", HttpStatus.INTERNAL_SERVER_ERROR.name());
        body.put("message", e.getMessage());
        log.error("Something went wrong retrieving the asset", e, request);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(AssetNotFoundException.class)
    private ResponseEntity<Object> handle(AssetNotFoundException e, WebRequest request) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", HttpStatus.NOT_FOUND.name());
        body.put("message", e.getMessage());
        log.error("No asset found for request", e, request);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
