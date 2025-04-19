package com.lazywallet.lazywallet.exceptions;

import java.util.List;

public class ErrorResponse {
    private String message;
    private List<String> details;
    private int status;

    public ErrorResponse(String message, List<String> details, int status) {
        this.message = message;
        this.details = details;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getDetails() {
        return details;
    }

    public int getStatus() {
        return status;
    }
}
