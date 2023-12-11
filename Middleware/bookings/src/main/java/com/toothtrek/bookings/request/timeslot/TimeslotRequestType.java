package com.toothtrek.bookings.request.timeslot;

public enum TimeslotRequestType {
    CREATE,
    CANCEL,
    GET;

    // The corresponding topic for each enum, see fromTopic()
    // (e.g. CREATE -> "create")
    private final String topic;

    /**
     * Timeslot Request Enum Constructor
     */
    TimeslotRequestType() {
        this.topic = this.name().toLowerCase();
    }

    /**
     * Returns the topic
     * 
     * @return
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Returns the TimeslotRequest enum from the topic
     * 
     * @param text the topic
     * @return TimeslotRequest enum or null if not found
     */
    public static TimeslotRequestType fromString(String text) {
        for (TimeslotRequestType request : TimeslotRequestType.values()) {
            if (request.getTopic().equalsIgnoreCase(text)) {
                return request;
            }
        }
        return null;
    }
}
