package com.toothtrek.notifications.request.notification;

public enum NotificationRequestType {
    // The enum values correspond to the topics
    SEND;

    // The corresponding topic for each enum, see fromTopic()
    // (e.g. CREATE -> "create")
    private final String topic;

    /**
     * Template Request Enum Constructor
     */
    NotificationRequestType() {
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
     * Returns the TemplateRequest enum from the topic
     * 
     * @param text the topic
     * @return TemplateRequest enum or null if not found
     */
    public static NotificationRequestType fromString(String text) {
        for (NotificationRequestType request : NotificationRequestType.values()) {
            if (request.getTopic().equalsIgnoreCase(text)) {
                return request;
            }
        }
        return null;
    }
}
