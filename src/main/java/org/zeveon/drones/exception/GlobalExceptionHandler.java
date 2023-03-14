package org.zeveon.drones.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.zeveon.drones.dto.ErrorMessageDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;

/**
 * @author Stanislav Vafin
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageDto> handleValidationException(MethodArgumentNotValidException exception, ServletWebRequest request) {
        var details = ofNullable(exception.getDetailMessageArguments())
                .map(args -> Arrays.stream(args)
                        .filter(o -> o instanceof ArrayList<?>)
                        .map(o -> (ArrayList<?>) o)
                        .flatMap(Collection::stream)
                        .map(Object::toString)
                        .toList())
                .orElse(singletonList(exception.getMessage()));
        return ResponseEntity.badRequest()
                .body(buildErrorMessage(exception, request, exception.getStatusCode().value(), details));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorMessageDto> handleValidationException(BindException exception, ServletWebRequest request) {
        var details = exception.getAllErrors().stream()
                .map(o -> "%s: '%s'".formatted(o.getObjectName(), o.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest()
                .body(buildErrorMessage(exception, request, HttpStatus.BAD_REQUEST.value(), details));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorMessageDto> handleValidationException(IOException exception, ServletWebRequest request) {
        var details = singletonList(ofNullable(exception.getCause())
                .map(Throwable::getMessage)
                .orElse(null));
        return ResponseEntity.internalServerError()
                .body(buildErrorMessage(exception, request, HttpStatus.INTERNAL_SERVER_ERROR.value(), details));
    }

    private ErrorMessageDto buildErrorMessage(Exception exception, ServletWebRequest request, int statusCode, List<String> details) {
        return ErrorMessageDto.builder()
                .timestamp(LocalDateTime.now())
                .status(statusCode)
                .path(request.getRequest().getServletPath())
                .exception(exception.getClass().getName())
                .message(exception.getMessage())
                .details(details)
                .build();
    }
}
