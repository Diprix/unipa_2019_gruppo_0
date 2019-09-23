package it.eng.unipa.filesharing.service;

import it.eng.unipa.filesharing.dto.SubscriptionDTO;
import it.eng.unipa.filesharing.model.WebPushSubscription;
import nl.martijndwars.webpush.Subscription;

import java.util.Collection;
import java.util.List;

public interface SubscriptionsRegistryService {

  List<WebPushSubscription> mySubscriptions();

    WebPushSubscription addSubscriptions(String userEmail, Subscription subscription);

    String saveSub(SubscriptionDTO subscriptionDTO);

    void removeSubscriptions(String userEmail, Subscription subscription);

    Collection<WebPushSubscription> getSubscriptions(String userEmal);

}
