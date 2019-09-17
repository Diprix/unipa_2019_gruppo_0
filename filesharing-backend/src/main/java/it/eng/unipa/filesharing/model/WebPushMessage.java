package it.eng.unipa.filesharing.model;

import java.util.UUID;

public class WebPushMessage {

    private final UUID uuid;
    private final String description;
    private  Object endPoint = null;


    public WebPushMessage(UUID creator, String description, Object endPoint) {
        this.uuid = UUID.randomUUID();
        this.description = description;
        this.endPoint = endPoint;
    }
}
