/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class DataErrorResponse
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.*;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataErrorResponse {

    private Map<String, Object> status;
    private String message;
    private String debugMessage;
    private Date date;

    private List<FieldValidationError> fieldValidationErrors;

    DataErrorResponse() {
        this.date = new Date();
    }

    DataErrorResponse(HttpStatus status) {
        this.status = setStatus(status);
        this.date = new Date();
    }

    DataErrorResponse(HttpStatus status, String message) {
        this.status = setStatus(status);
        this.message = message;
        this.date = new Date();
    }

    DataErrorResponse(HttpStatus status, Throwable ex) {
        this.status = setStatus(status);
        this.message = "Unexpected error";
        this.date = new Date();
        this.setDebugMessage(ex.toString());
    }

    DataErrorResponse(HttpStatus status, String message, Throwable ex) {
        this.status = setStatus(status);
        this.message = message;
        this.date = new Date();
        this.setDebugMessage(ex.toString());
    }

    void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(error -> {
            FieldValidationError subError = new FieldValidationError();
            subError.setField(error.getField());
            subError.setMessage(error.getDefaultMessage());
            subError.setRejectedValue(error.getRejectedValue());
            subError.setObject(error.getObjectName());
            this.addSubError(subError);
        });
    }

    private void addSubError(FieldValidationError subError) {
        if (fieldValidationErrors == null) {
            fieldValidationErrors = new ArrayList<>();
        }
        fieldValidationErrors.add(subError);
    }

    public Map<String, Object> setStatus(HttpStatus status) {
        Map<String, Object> statusMap = new HashMap<>();
        statusMap.put("value", status.value());
        statusMap.put("reasonPhrase", status.getReasonPhrase());
        statusMap.put("name", status.name());
        statusMap.put("ordinal", status.ordinal());
        return statusMap;
    }

    @Override
    public String toString() {
        return "\n" + "DataErrorResponse{" + "\n" +
                "status=" + status + "," + "\n" +
                "message='" + message + "," + "\n" +
                "debugMessage='" + debugMessage + "," + "\n" +
                "date=" + date + "," + "\n" +
                "fieldValidationErrors=" + fieldValidationErrors +
                '}';
    }

}
