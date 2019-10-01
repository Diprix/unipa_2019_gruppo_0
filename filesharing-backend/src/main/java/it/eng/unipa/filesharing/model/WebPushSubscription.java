package it.eng.unipa.filesharing.model;

import javax.persistence.*;
import java.util.UUID;


@Entity
public class WebPushSubscription  {

    @Id
    @SequenceGenerator(name="webPushSubscription_seq", initialValue=1, allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="webPushSubscription_seq")
    private Long id;

    private String email;
    private String auth;
    private String endpoint;
    private String p256dh;
    private UUID uuid;

    public WebPushSubscription() {
    }

    public WebPushSubscription(String email, String endpoint, String auth, String p256dh, UUID uuid) {
        this.email = email;
        this.auth = auth;
        this.endpoint = endpoint;
        this.p256dh = p256dh;
        this.uuid = uuid;
    }

    public WebPushSubscription(String email, String endpoint, String auth, String p256dh) {
        this.email = email;
        this.auth = auth;
        this.endpoint = endpoint;
        this.p256dh = p256dh;
        // da eliminare
        this.uuid = UUID.randomUUID();
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

    public String getP256dh() {
        return p256dh;
    }

    public void setP256dh(String p256dh) {
        this.p256dh = p256dh;
    }

    public String getEndpoint() {
        return endpoint;
    }
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "WebPushSubscription{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", auth='" + auth + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", p256dh='" + p256dh + '\'' +
                ", uuid=" + uuid +
                '}';
    }
}
