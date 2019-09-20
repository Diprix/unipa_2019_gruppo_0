package it.eng.unipa.filesharing.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class WebPushSubscription implements Serializable {

    @Id
    @SequenceGenerator(name="membershipSubscription_seq", initialValue=1, allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="membership_Subscription")
    private Long id;

    //@OneToOne
    @JoinColumn(name = "email")
    private String email;
    private String auth;
    private String endpoint;
    private String p256dh;
    //private Keys keys;

   // public static  class Keys {


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

//    public Keys getKeys() {
//        return keys;
//    }
//
//    public void setKeys(Keys keys) {
//        this.keys = keys;
//    }


}
