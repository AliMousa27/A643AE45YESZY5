package com.toothtrek.bookings.response;

/**
 * This enum is used to represent the status of a response.
 */
public enum ResponseStatus {
    SUCCESS("success"),
    ERROR("error"),
    EMPTY("empty"); // Whenever a get request returns no results

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