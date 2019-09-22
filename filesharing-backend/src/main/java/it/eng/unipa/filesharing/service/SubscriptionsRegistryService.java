package it.eng.unipa.filesharing.service;

import it.eng.unipa.filesharing.dto.SubscriptionDTO;
import it.eng.unipa.filesharing.model.WebPushSubscription;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.Subscription;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface SubscriptionsRegistryService {

    List<SubscriptionDTO> mySubscription();

    List<SubscriptionDTO> mySubscription(String email);

    UUID save(SubscriptionDTO webPushSubscription);
    SubscriptionDTO get(UUID uuid);

    //RICEVE L'OGGETTO Subscription DAL PUSH SERVICE E LO FORMATTA CON L'EMAIL IN UN DTO
    WebPushSubscription addSubscriptions(String userEmail, Subscription subscription);

    SubscriptionDTO removeSubscriptions(String userEmail, Subscription subscription);

    Collection<WebPushSubscription> getSubscriptions(String userEmal);

}
