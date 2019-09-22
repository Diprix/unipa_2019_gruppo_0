package it.eng.unipa.filesharing.dto;

import it.eng.unipa.filesharing.model.WebPushSubscription;
import nl.martijndwars.webpush.Subscription;


public class SubscriptionDTO {
    private String email;
    private WebPushSubscription webPushSubscription;


    public SubscriptionDTO(String email, WebPushSubscription webPushSubscription) {
        this.email = email;
        this.webPushSubscription = webPushSubscription;
    }

    public SubscriptionDTO(String email, Subscription subscription) {
        super();
        this.email = email;
        this.webPushSubscription.setEmail(email);
        this.webPushSubscription.setEndpoint(subscription.endpoint);
        this.webPushSubscription.setAuth(subscription.keys.auth);
        this.webPushSubscription.setP256dh(subscription.keys.p256dh);
    }

    public SubscriptionDTO() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public WebPushSubscription getWebPushSubscription() {
        return webPushSubscription;
    }

    public void setWebPushSubscription(WebPushSubscription webPushSubscription) {
        this.webPushSubscription = webPushSubscription;
    }
}
