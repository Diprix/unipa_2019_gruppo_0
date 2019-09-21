package it.eng.unipa.filesharing.dto;

import it.eng.unipa.filesharing.model.WebPushSubscription;


public class SubscriptionDTO {
    private String email;
    private WebPushSubscription notification;


    public WebPushSubscription getNotification() {
        return notification;
    }

    public void setNotification(WebPushSubscription notification) {
        this.notification = notification;
    }


    public SubscriptionDTO() {
    }

    public SubscriptionDTO(String email, WebPushSubscription notification) {
        super();
        this.email = email;
        this.notification = notification;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
