package com.toothtrek.notifications.response;

/**
 * This enum is used to represent the status of a response.
 */
public enum ResponseStatus {
    SUCCESS("success"),
    ERROR("error");

    private final String status;

    ResponseStatus(String status) {
        this.status = status;
    }

    /**
     * Returns the status as a string.
     */
    public String toString() {
        return status;
    }

}