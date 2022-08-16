/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class ExceptionControllerAdvice
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.vk.competition.minbenchmark.dto.ExceptionResponseBodyDto;
import ru.vk.competition.minbenchmark.dto.SingleQueryDto;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@ControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    private final String ADD_NEW_QUERY = "/api/single-query/add-new-query";
    private final String GET_SINGLE_QUERY_BY_ID = "/api/single-query/get-single-query-by-id/";

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public final ResponseEntity<?> handleNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        ExceptionResponseBodyDto err = new ExceptionResponseBodyDto(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage());
        log.error("",ex);
        return new ResponseEntity<>(err, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<?> handleNotReadableException(HttpMessageNotReadableException ex, ServletWebRequest request) {
        HttpStatus status;
        if (ADD_NEW_QUERY.equalsIgnoreCase(request.getRequest().getServletPath())) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.NOT_ACCEPTABLE;
        }
        ExceptionResponseBodyDto err = new ExceptionResponseBodyDto(request, status);
        log.error("",ex);
        return new ResponseEntity<>(err, status);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public final ResponseEntity<?> handleConversionException(HttpMessageConversionException ex, ServletWebRequest request) {
        ExceptionResponseBodyDto err = new ExceptionResponseBodyDto(request, HttpStatus.BAD_REQUEST);
        log.error("",ex);
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, ServletWebRequest request) {
        String requestPath = request.getRequest().getServletPath();
        HttpStatus status;
        if (requestPath.contains(GET_SINGLE_QUERY_BY_ID)) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            status = HttpStatus.NOT_ACCEPTABLE;
        }
        ExceptionResponseBodyDto err = new ExceptionResponseBodyDto(request, status);
        log.error("",ex);
        return new ResponseEntity<>(err, status);
    }

    @ExceptionHandler(HttpMessageNotWritableException.class)
    public final ResponseEntity<?> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, ServletWebRequest request) {
        ExceptionResponseBodyDto err = new ExceptionResponseBodyDto(request,
                HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("",ex);
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Обработка исключений на валидные поля
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, ServletWebRequest request) {
        HttpStatus status;
        String singleQueryDto = SingleQueryDto.class.getSimpleName();
        String bindingResult = ex.getBindingResult().getObjectName();
        if (singleQueryDto.equalsIgnoreCase(bindingResult)) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.NOT_ACCEPTABLE;
            DataErrorResponse response = new DataErrorResponse(status, "Method arg not valid!", ex);
            response.addValidationErrors(ex.getBindingResult().getFieldErrors());
            log.error("Incorrect JSON request. MethodArgumentNotValidException = {}", response.toString(), ex);
            return new ResponseEntity<>(response, status);
        }
        ExceptionResponseBodyDto response = new ExceptionResponseBodyDto(request, status);
        log.error("",ex);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<?> constraintViolationHandler(ConstraintViolationException ex) {
        DataErrorResponse response = new DataErrorResponse(HttpStatus.NOT_ACCEPTABLE, "Validation error!", ex);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        List<FieldError> fieldErrors = getFields(constraintViolations);
        String objectValid = constraintViolations.iterator().next().getPropertyPath().iterator().next().getKind().toString();
        log.info("fieldValidationErrors: {} - {}", objectValid, fieldErrors);
        response.addValidationErrors(fieldErrors);
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    public List<FieldError> getFields(Set<ConstraintViolation<?>> constraintViolations) {
        return constraintViolations.stream()
                .map(constraintViolation -> new FieldError(constraintViolation.getPropertyPath().toString(),  // объект ошибки
                        constraintViolation.getPropertyPath().toString().substring(
                                constraintViolation.getPropertyPath().toString().indexOf(".") + 1), // поле ошибки
                        constraintViolation.getInvalidValue().toString(), // ошибочное заполнение
                        false, null, null,
                        constraintViolation.getMessage())).collect(Collectors.toList());    // сообщение от валидатора
    }

}
