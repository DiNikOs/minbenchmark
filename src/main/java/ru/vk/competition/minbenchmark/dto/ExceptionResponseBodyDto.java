/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class ExceptionResponseBodyDto
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDateTime;

@Data
public class ExceptionResponseBodyDto {

    private String timestamp;
    private String path;
    private int status;
    private String error;
    private String requestId;

    public ExceptionResponseBodyDto(int status, String error) {
        this.status = status;
        this.error = error;
        this.timestamp = LocalDateTime.now().toString();
    }

    public ExceptionResponseBodyDto(String path, int status, String error, String requestId) {
        this.timestamp = LocalDateTime.now().toString();
        this.path = path;
        this.status = status;
        this.error = error;
        this.requestId = requestId;
    }

    public ExceptionResponseBodyDto(ServletWebRequest request, HttpStatus status) {
        this.timestamp = LocalDateTime.now().toString();
        this.path = request.getRequest().getServletPath();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.requestId = request.getRequest().getRequestedSessionId();
    }
}
