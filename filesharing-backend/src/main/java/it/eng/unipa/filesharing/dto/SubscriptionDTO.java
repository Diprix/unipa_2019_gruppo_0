package it.eng.unipa.filesharing.dto;

import it.eng.unipa.filesharing.model.WebPushSubscription;


public class SubscriptionDTO {
    private String email;
    private WebPushSubscription webPushSubscription;


    public WebPushSubscription getWebPushSubscription() {
        return webPushSubscription;
    }

   // DA SPOSTARE NEL REGISTRY
    public void setWebPushSubscription(WebPushSubscription webPushSubscription) {
        this.webPushSubscription = webPushSubscription;
    }


    public SubscriptionDTO() {
    }

    public SubscriptionDTO(String email, WebPushSubscription webPushSubscription) {
        super();
        this.email = email;
        this.webPushSubscription = webPushSubscription;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
