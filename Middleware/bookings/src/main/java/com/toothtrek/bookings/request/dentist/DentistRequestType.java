package com.toothtrek.bookings.request.dentist;

public enum DentistRequestType {
    CREATE,
    GET,
    UPDATE,
    DELETE;

    // The corresponding topic for each enum, see fromTopic()
    // (e.g. CREATE -> "create")
    private final String topic;

    /**
     * dentist Request Enum Constructor
     */
    DentistRequestType() {
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
     * Returns the DentistRequest enum from the topic
     * 
     * @param text the topic
     * @return DentistRequest enum or null if not found
     */
    public static DentistRequestType fromString(String text) {
        for (DentistRequestType request : DentistRequestType.values()) {
            if (request.getTopic().equalsIgnoreCase(text)) {
                return request;
            }
        }
        return null;
    }
}
