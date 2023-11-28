package com.toothtrek.bookings.request.booking;

public enum BookingRequestType {
    CREATE,
    CANCEL,
    GET,
    CONFIRM,
    REJECT;

    // The corresponding topic for each enum, see fromTopic()
    // (e.g. CREATE -> "create")
    private final String topic;

    /**
     * Booking Request Enum Constructor
     */
    BookingRequestType() {
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
     * Returns the BookingRequest enum from the topic
     * 
     * @param text the topic
     * @return BookingRequest enum or null if not found
     */
    public static BookingRequestType fromString(String text) {
        for (BookingRequestType request : BookingRequestType.values()) {
            if (request.getTopic().equalsIgnoreCase(text)) {
                return request;
            }
        }
        return null;
    }
}
