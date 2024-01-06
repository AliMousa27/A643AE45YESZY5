package com.toothtrek.bookings.request.patient;

public enum PatientRequestType {
    CREATE,
    UPDATE,
    GET;

    // The corresponding topic for each enum, see fromTopic()
    // (e.g. CREATE -> "create")
    private final String topic;

    /**
     * Patient Request Enum Constructor
     */
    PatientRequestType() {
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
     * Returns the PatientRequest enum from the topic
     * 
     * @param text the topic
     * @return PatientRequest enum or null if not found
     */
    public static PatientRequestType fromString(String text) {
        for (PatientRequestType request : PatientRequestType.values()) {
            if (request.getTopic().equalsIgnoreCase(text)) {
                return request;
            }
        }
        return null;
    }
}
