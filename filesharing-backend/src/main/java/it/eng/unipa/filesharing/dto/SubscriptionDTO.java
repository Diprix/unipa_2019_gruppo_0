package it.eng.unipa.filesharing.dto;

import it.eng.unipa.filesharing.model.WebPushSubscription;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SubscriptionDTO {
    private String email;
    private WebPushSubscription notification;
    private List<NotifyDTO> notificationPart = new ArrayList<>();


    public SubscriptionDTO() {
    }

    public SubscriptionDTO(String email, List<NotifyDTO> notificationPart) {
        this.email = email;
        this.notificationPart = notificationPart;
    }
}
