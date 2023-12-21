package com.toothtrek.bookings.request.office;

public enum OfficeRequestType {
    CREATE,
    GET,
    UPDATE,
    DELETE;

    // The corresponding topic for each enum, see fromTopic()
    // (e.g. CREATE -> "create")
    private final String topic;

    /**
     * Office Request Enum Constructor
     */
    OfficeRequestType() {
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
     * Returns the OfficeRequest enum from the topic
     * 
     * @param text the topic
     * @return OfficeRequest enum or null if not found
     */
    public static OfficeRequestType fromString(String text) {
        for (OfficeRequestType request : OfficeRequestType.values()) {
            if (request.getTopic().equalsIgnoreCase(text)) {
                return request;
            }
        }
        return null;
    }
}
