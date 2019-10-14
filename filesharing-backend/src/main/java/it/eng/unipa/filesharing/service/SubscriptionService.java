package it.eng.unipa.filesharing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.eng.unipa.filesharing.dto.SubscriptionDTO;
import it.eng.unipa.filesharing.model.PushSelector;
import it.eng.unipa.filesharing.model.UserRole;
import it.eng.unipa.filesharing.model.WebPushSubscription;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.Subscription;
import org.jose4j.lang.JoseException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface SubscriptionService {

    List<SubscriptionDTO> mySubscriptionsDTO(String email, UUID uuid);

    List<WebPushSubscription> mySubscriptions(String email);


    // ADD SUBSCRIPTION TO DATABASE
    WebPushSubscription addSubscriptions(String userEmail, Subscription subscription, UUID uuid);

    // REMOVE SUBSCRIPTION TO DATABASE
    void removeSubscriptions(Subscription subscription);

    // RETURN A SUBSCRIPTION TO DATABASE
    Notification getSubscriptions(String userEmail, String endPoint, String nameFile);

    // SEND PUSH MESSAGE TO USERS SUBSCRITPED
    void sendPushMessage(String userMail, WebPushSubscription webPushSubscription, String nameFile, UUID uuid) throws NoSuchProviderException, InvalidKeySpecException,
            JsonProcessingException, JoseException, IOException, GeneralSecurityException, ExecutionException, InterruptedException, NoSuchAlgorithmException;

    SubscriptionDTO tree();


    SubscriptionDTO saveSub(Subscription subscription);

    void setPushAction(PushSelector pushSelector) throws IOException, GeneralSecurityException, InterruptedException, JoseException, ExecutionException;
}
