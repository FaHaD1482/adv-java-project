package com.posapp.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    private String error;
    private Integer status;
    private LocalDateTime timestamp;
    private List<String> details;

    public ErrorResponse() {}

    public ErrorResponse(String error, Integer status) {
        this.error = error;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(String error, Integer status, LocalDateTime timestamp, List<String> details) {
        this.error = error;
        this.status = status;
        this.timestamp = timestamp;
        this.details = details;
    }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public List<String> getDetails() { return details; }
    public void setDetails(List<String> details) { this.details = details; }
}
