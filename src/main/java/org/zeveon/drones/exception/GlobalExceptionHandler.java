package org.zeveon.drones.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.zeveon.drones.dto.ErrorMessageDto;
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
                .map(o -> DETAILS_FORMAT.formatted(o.getObjectName(), o.getDefaultMessage()))
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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessageDto> handleValidationException(ConstraintViolationException exception, ServletWebRequest request) {
        ensureImageConsistency();
        var details = exception.getConstraintViolations().stream()
                .map(cv -> DETAILS_FORMAT.formatted(
                        isNotBlank(cv.getPropertyPath().toString())
                                ? cv.getPropertyPath().toString()
                                : cv.getRootBeanClass().getName(),
                        cv.getMessage()))
                .toList();
        return ResponseEntity.badRequest()
                .body(buildErrorMessage(exception, request, HttpStatus.BAD_REQUEST.value(), details));
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<ErrorMessageDto> handleValidationException(PSQLException exception, ServletWebRequest request) {
        ensureImageConsistency();
        var details = singletonList(ofNullable(exception.getServerErrorMessage())
                .map(m -> DETAILS_FORMAT.formatted(m.getColumn(), ITEMS_FORMAT.formatted(m.getMessage(), m.getDetail())))
                .orElse(null));
        return ResponseEntity.badRequest()
                .body(buildErrorMessage(exception, request, HttpStatus.BAD_REQUEST.value(), details));
    }

    private void ensureImageConsistency() {
        var medicationImagePaths = medicationService.getAllExistingMedicationImagePaths();
        imageService.getAllFilePaths().stream()
                .filter(p -> !medicationImagePaths.contains(p))
                .forEach(imageService::remove);
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
