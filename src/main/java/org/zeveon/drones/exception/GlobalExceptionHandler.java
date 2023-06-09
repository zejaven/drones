package org.zeveon.drones.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.zeveon.drones.dto.ErrorMessageDto400;
import org.zeveon.drones.dto.ErrorMessageDto500;
import org.zeveon.drones.service.ImageService;
import org.zeveon.drones.service.MedicationService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

/**
 * @author Stanislav Vafin
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final String DETAILS_FORMAT = "%s: '%s'";
    private static final String ITEMS_FORMAT = "%s, %s";

    private final MedicationService medicationService;

    private final ImageService imageService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessageDto400> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, ServletWebRequest request) {
        var details = ofNullable(exception.getDetailMessageArguments())
                .map(args -> Arrays.stream(args)
                        .filter(o -> o instanceof ArrayList<?>)
                        .map(o -> (ArrayList<?>) o)
                        .flatMap(Collection::stream)
                        .map(Object::toString)
                        .toList())
                .orElse(singletonList(exception.getMessage()));
        return ResponseEntity.badRequest()
                .body(buildErrorMessage400(exception, request, exception.getStatusCode().value(), details));
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessageDto400> handleBindException(BindException exception, ServletWebRequest request) {
        var details = exception.getAllErrors().stream()
                .filter(o -> o instanceof FieldError)
                .map(o -> (FieldError) o)
                .map(o -> DETAILS_FORMAT.formatted(o.getField(), o.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest()
                .body(buildErrorMessage400(exception, request, HttpStatus.BAD_REQUEST.value(), details));
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessageDto500> handleIOException(IOException exception, ServletWebRequest request) {
        var details = singletonList(ofNullable(exception.getCause())
                .map(Throwable::getMessage)
                .orElse(null));
        return ResponseEntity.internalServerError()
                .body(buildErrorMessage500(exception, request, HttpStatus.INTERNAL_SERVER_ERROR.value(), details));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessageDto500> handleRuntimeException(RuntimeException exception, ServletWebRequest request) {
        ensureImageConsistency();
        var details = singletonList(ofNullable(exception.getCause())
                .map(Throwable::getMessage)
                .orElse(null));
        return ResponseEntity.internalServerError()
                .body(buildErrorMessage500(exception, request, HttpStatus.INTERNAL_SERVER_ERROR.value(), details));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessageDto400> handleConstraintViolationException(ConstraintViolationException exception, ServletWebRequest request) {
        ensureImageConsistency();
        var details = exception.getConstraintViolations().stream()
                .map(cv -> DETAILS_FORMAT.formatted(
                        isNotBlank(cv.getPropertyPath().toString())
                                ? cv.getPropertyPath().toString()
                                : cv.getRootBeanClass().getName(),
                        cv.getMessage()))
                .toList();
        return ResponseEntity.badRequest()
                .body(buildErrorMessage400(exception, request, HttpStatus.BAD_REQUEST.value(), details));
    }

    @ExceptionHandler(PSQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessageDto400> handlePSQLException(PSQLException exception, ServletWebRequest request) {
        ensureImageConsistency();
        var details = singletonList(ofNullable(exception.getServerErrorMessage())
                .map(m -> DETAILS_FORMAT.formatted(m.getColumn(), ITEMS_FORMAT.formatted(m.getMessage(), m.getDetail())))
                .orElse(null));
        return ResponseEntity.badRequest()
                .body(buildErrorMessage400(exception, request, HttpStatus.BAD_REQUEST.value(), details));
    }

    private void ensureImageConsistency() {
        var medicationImagePaths = medicationService.getAllExistingMedicationImagePaths();
        imageService.getAllFilePaths().stream()
                .filter(p -> !medicationImagePaths.contains(p))
                .forEach(imageService::remove);
    }

    private ErrorMessageDto400 buildErrorMessage400(Exception exception, ServletWebRequest request, int statusCode, List<String> details) {
        return ErrorMessageDto400.builder()
                .timestamp(LocalDateTime.now())
                .status(statusCode)
                .path(request.getRequest().getServletPath())
                .exception(exception.getClass().getName())
                .message(exception.getMessage())
                .details(details)
                .build();
    }

    private ErrorMessageDto500 buildErrorMessage500(Exception exception, ServletWebRequest request, int statusCode, List<String> details) {
        return ErrorMessageDto500.builder()
                .timestamp(LocalDateTime.now())
                .status(statusCode)
                .path(request.getRequest().getServletPath())
                .exception(exception.getClass().getName())
                .message(exception.getMessage())
                .details(details)
                .build();
    }
}
