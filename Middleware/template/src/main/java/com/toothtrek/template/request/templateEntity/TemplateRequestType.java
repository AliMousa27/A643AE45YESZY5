package com.toothtrek.template.request.templateEntity;

public enum TemplateRequestType {
    // The enum values correspond to the topics
    CREATE,
    GET;

    // The corresponding topic for each enum, see fromTopic()
    // (e.g. CREATE -> "create")
    private final String topic;

    /**
     * Template Request Enum Constructor
     */
    TemplateRequestType() {
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
    public static TemplateRequestType fromString(String text) {
        for (TemplateRequestType request : TemplateRequestType.values()) {
            if (request.getTopic().equalsIgnoreCase(text)) {
                return request;
            }
        }
        return null;
    }
}
