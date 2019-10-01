package it.eng.unipa.filesharing.dto;

import it.eng.unipa.filesharing.model.WebPushSubscription;
import nl.martijndwars.webpush.Subscription;

import java.util.UUID;


public class SubscriptionDTO {
    private String email;
    private String auth;
    private String endpoint;
    private String p256dh;
    private UUID uuid;


    public SubscriptionDTO() {

    }

    public SubscriptionDTO(String email, String auth, String endpoint, String p256dh, UUID uuid) {
        this.email = email;
        this.auth = auth;
        this.endpoint = endpoint;
        this.p256dh = p256dh;
        this.uuid =uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getP256dh() {
        return p256dh;
    }

    public void setP256dh(String p256dh) {
        this.p256dh = p256dh;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "SubscriptionDTO{" +
                "email='" + email + '\'' +
                ", auth='" + auth + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", p256dh='" + p256dh + '\'' +
                ", uuid='" +uuid + '\''+
                '}';
    }
}



