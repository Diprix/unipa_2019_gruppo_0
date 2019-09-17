package it.eng.unipa.filesharing.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;
//@Entity
public class WebPushMessage {
   // @Id
    private final UUID uuid;

    private  String description= null;
    private  Object endPoint = null;

    //IMPLEMENT FOREIN KEY CONSTRACT

    public WebPushMessage(UUID uuid) {
        // TODO Auto-generated constructor stub
        this.uuid = UUID.randomUUID();
    }

    public WebPushMessage(UUID creator, String description, Object endPoint) {
        this.uuid = UUID.randomUUID();
        this.description = description;
        this.endPoint = endPoint;
    }
}
