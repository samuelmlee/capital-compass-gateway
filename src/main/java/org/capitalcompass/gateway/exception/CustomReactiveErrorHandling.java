package org.capitalcompass.gateway.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class CustomReactiveErrorHandling {

    @ExceptionHandler(UsersClientErrorException.class)
    public ResponseEntity<String> handleUsersClientErrorException(UsersClientErrorException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

    @ExceptionHandler(StocksClientErrorException.class)
    public ResponseEntity<String> handleStocksClientErrorException(StocksClientErrorException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }
}