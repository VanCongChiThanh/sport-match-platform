package com.microbase.commonlibrary.exception;

import com.microbase.commonlibrary.dto.ErrorDetail;
import com.microbase.commonlibrary.dto.ErrorResponse;
import com.microbase.commonlibrary.error.ErrorMessageResolver;
import com.microbase.commonlibrary.utils.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.rmi.ServerError;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String SERVER_ERROR_CODE = "ERR.SERVER";
    private static final String BAD_REQUEST_CODE = "ERR.BAD_REQUEST";

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(NotFoundException ex, HttpServletRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> alreadyExistsException(AlreadyExistsException ex, HttpServletRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> forbiddenException(ForbiddenException ex, HttpServletRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestException(BadRequestException ex, HttpServletRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ServerError.class)
    public ResponseEntity<ErrorResponse> serverError(ServerError ex, HttpServletRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        String message = ex.getName()
                + " should be of type "
                + Objects.requireNonNull(ex.getRequiredType()).getSimpleName();
        ErrorResponse error = ErrorResponse.of(BAD_REQUEST_CODE, message, HttpStatus.BAD_REQUEST.value(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        String message = ex.getMessage();
        String entityType = "entity";
        if (message != null && message.contains("not found with")) {
            String[] parts = message.split(" not found with");
            if (parts.length > 0) {
                entityType = parts[0].trim().toUpperCase();
            }
        }
        ErrorResponse error = ErrorResponse.of(entityType + ".NOT_FOUND", message, HttpStatus.NOT_FOUND.value(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.of(
                SERVER_ERROR_CODE,
                "Unexpected server error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        List<ErrorDetail> details = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toErrorDetail)
                .toList();

        ErrorDetail firstError = details.isEmpty()
                ? new ErrorDetail(null, BAD_REQUEST_CODE, "Invalid request")
                : details.get(0);
        String path = request instanceof ServletWebRequest servletWebRequest
                ? servletWebRequest.getRequest().getRequestURI()
                : null;
        ErrorResponse errorResponse = ErrorResponse.of(
                firstError.code(),
                firstError.message(),
                HttpStatus.BAD_REQUEST.value(),
                path,
                details
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private ErrorDetail toErrorDetail(FieldError fieldError) {
        String error = StringUtils.toSnakeCase(Objects.requireNonNull(fieldError.getCode()));
        String fieldName = StringUtils.toSnakeCase(fieldError.getField());
        String resource = StringUtils.toSnakeCase(fieldError.getObjectName());
        ErrorResponse resolvedError = ErrorMessageResolver.getValidationError(resource, fieldName, error);
        return new ErrorDetail(fieldError.getField(), resolvedError.code(), resolvedError.message());
    }

    private ResponseEntity<ErrorResponse> buildResponse(String errorKey, HttpStatus status, HttpServletRequest request) {
        ErrorResponse resolvedError = ErrorMessageResolver.getExceptionError(errorKey);
        ErrorResponse error = ErrorResponse.of(
                resolvedError.code(),
                resolvedError.message(),
                status.value(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, status);
    }
}