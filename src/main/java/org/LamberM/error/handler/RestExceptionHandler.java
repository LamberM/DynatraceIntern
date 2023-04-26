package org.LamberM.error.handler;

import org.LamberM.controllers.ProvideController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<RestErrorResponse> handleConflict(IllegalArgumentException exception) {
        return ResponseEntity.badRequest()
                .body(new RestErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }

    @ExceptionHandler(value = {ProvideController.RestValidationException.class})
    protected ResponseEntity<RestErrorResponse> handleRestValidationException(ProvideController.RestValidationException exception) {
        return ResponseEntity.badRequest()
                .body(new RestErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }

    @ExceptionHandler(value = {Throwable.class})
    protected ResponseEntity<RestErrorResponse> handleInternalServerError(Throwable throwable) {
        return ResponseEntity.badRequest()
                .body(new RestErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "internal error"));
    }

}
