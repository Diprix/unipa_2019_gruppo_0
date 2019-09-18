package it.eng.unipa.filesharing.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class MemberSubscription implements Serializable {

   @Id
   @SequenceGenerator(name="membershipSubscription_seq", initialValue=1, allocationSize=1)
   @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="membership_Subscription")
    private Long id;

    private String email;

    private String endpoint;
   // private WebPushSubscription.Keys keys;

    public MemberSubscription() {

    }

    public MemberSubscription(String email, String endpoint) {
        this.email = email;
        this.endpoint = endpoint;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

}
