package ru.practicum.statservice.exeption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

@RestControllerAdvice
@Slf4j
public class ExceptionsHandler {

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<Error> internalServerErrorException(HttpServerErrorException.InternalServerError exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Error(exception.getMessage()));
    }

    @Getter
    @RequiredArgsConstructor
    public static class Error {
        private final String error;
    }

    @Getter
    @RequiredArgsConstructor
    public static class ErrorMessage {
        private final String message;
    }
}
