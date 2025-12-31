package com.sportmatch.profileservice.exception;

import com.sportmatch.profileservice.common.response.ErrorResponse;
import com.sportmatch.profileservice.common.util.CommonFunction;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.rmi.ServerError;

import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String SERVER_ERROR_CODE = "ERR.SERVER";

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<ErrorResponse> profileNotFoundException(
            ProfileNotFoundException ex) {
        ErrorResponse error = CommonFunction.getExceptionError(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ProfileAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> profileAlreadyExistsException(
            ProfileAlreadyExistsException ex) {
        ErrorResponse error = CommonFunction.getExceptionError(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestException(
            BadRequestException ex) {
        ErrorResponse error = CommonFunction.getExceptionError(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ServerError.class)
    public ResponseEntity<ErrorResponse> serverError(ServerError ex) {
        ErrorResponse error = CommonFunction.getExceptionError(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        String msg =
                ex.getName()
                        + " should be of type "
                        + Objects.requireNonNull(ex.getRequiredType()).getName();

        ErrorResponse error = new ErrorResponse(SERVER_ERROR_CODE, msg);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        String message = e.getMessage();
        String entityType = "entity";
        if (message != null){
            if (message.contains("not found with")) {
                String[] parts = message.split(" not found with");
                if (parts.length > 0) {
                    entityType = parts[0].trim().toLowerCase();
                }
            }

        }
        String errorCode = entityType.toUpperCase() + ".NOT_FOUND";
        ErrorResponse error = new ErrorResponse(errorCode, message);

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        List<ObjectError> listError = ex.getBindingResult().getAllErrors();
        ObjectError objectError = listError.get(listError.size() - 1);
        String error = CommonFunction.convertToSnakeCase(Objects.requireNonNull(objectError.getCode()));
        String fieldName = CommonFunction.convertToSnakeCase(((FieldError) objectError).getField());
        String resource = CommonFunction.convertToSnakeCase(objectError.getObjectName());

        ErrorResponse errorResponse = CommonFunction.getValidationError(resource, fieldName, error);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}