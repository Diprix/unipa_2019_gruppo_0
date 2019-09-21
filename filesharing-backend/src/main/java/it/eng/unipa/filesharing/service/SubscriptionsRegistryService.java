package it.eng.unipa.filesharing.service;

import it.eng.unipa.filesharing.dto.SubscriptionDTO;
import it.eng.unipa.filesharing.model.WebPushSubscription;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface SubscriptionsRegistryService {

    List<SubscriptionDTO> mySubscription();

    List<SubscriptionDTO> mySubscription(String email);

    UUID save(SubscriptionDTO webPushSubscription);
    SubscriptionDTO get(UUID uuid);

    SubscriptionDTO addSubscriptions(String userEmail, WebPushSubscription webPushSubscription);
    SubscriptionDTO removeSubscriptions(String userEmail, WebPushSubscription webPushSubscription);

    Collection<WebPushSubscription> getSubscriptions(String userEmal);

}
