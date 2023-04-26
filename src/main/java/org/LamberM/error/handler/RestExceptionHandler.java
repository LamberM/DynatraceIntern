package org.LamberM.error.handler;

import org.LamberM.controllers.ExchangeRateController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<RestErrorResponse> handleConflict(IllegalArgumentException exception) {
        return ResponseEntity.badRequest()
                .body(new RestErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }

    @ExceptionHandler(value = {ExchangeRateController.RestValidationException.class})
    protected ResponseEntity<RestErrorResponse> handleRestValidationException(ExchangeRateController.RestValidationException exception) {
        return ResponseEntity.badRequest()
                .body(new RestErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }


    @ExceptionHandler(value = {Throwable.class})
    protected ResponseEntity<RestErrorResponse> handleInternalServerError(Throwable throwable) {
        return ResponseEntity.badRequest()
                .body(new RestErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "internal error"));
    }
    @ExceptionHandler(value = {HttpClientErrorException.class})
    protected ResponseEntity<RestErrorResponse> handleRestClientException(HttpClientErrorException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(new RestErrorResponse(ex.getStatusCode().value(), ex.getMessage()));
    }

}
