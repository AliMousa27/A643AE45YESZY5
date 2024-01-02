package com.toothtrek.dentalRecord.request.record;

public enum RecordRequestType {
    // The enum values correspond to the topics
    CREATE,
    GET,
    UPDATE;

    // The corresponding topic for each enum, see fromTopic()
    // (e.g. CREATE -> "create")
    private final String topic;

    /**
     * Template Request Enum Constructor
     */
    RecordRequestType() {
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
    public static RecordRequestType fromString(String text) {
        for (RecordRequestType request : RecordRequestType.values()) {
            if (request.getTopic().equalsIgnoreCase(text)) {
                return request;
            }
        }
        return null;
    }
}
