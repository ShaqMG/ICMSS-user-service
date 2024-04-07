package org.icmss.icmssuserservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.List;

@ControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    protected ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException exception, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                exception.getStatus(),
                exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, exception.getStatus());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<List<ExceptionResponse>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception, WebRequest request) {
        BindingResult bindingResult = exception.getBindingResult();
        List<ObjectError> objectErrorList = bindingResult.getAllErrors();
        List<ExceptionResponse> exceptionResponses = objectErrorList.stream()
                .map(objectError -> new ExceptionResponse(HttpStatus.BAD_REQUEST,
                        objectError.getDefaultMessage(),
                        request.getDescription(false)))
                .toList();

        return new ResponseEntity<>(exceptionResponses, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<ExceptionResponse> handleConstraintViolationException(
            ConstraintViolationException exception, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


}
