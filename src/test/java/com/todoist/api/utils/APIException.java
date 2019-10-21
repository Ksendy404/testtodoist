package com.todoist.api.utils;

public class APIException extends Exception {

    public APIException(int httpStatus) {
        super(String.format("Request failed with HTTP Status %d", httpStatus));
    }

    public APIException (String message) {
        super(message);
    }
}
