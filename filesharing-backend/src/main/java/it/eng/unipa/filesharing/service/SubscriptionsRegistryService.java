package it.eng.unipa.filesharing.service;

import it.eng.unipa.filesharing.dto.SubscriptionDTO;
import it.eng.unipa.filesharing.model.UserRole;
import it.eng.unipa.filesharing.model.WebPushSubscription;
import nl.martijndwars.webpush.Subscription;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SubscriptionsRegistryService {

  List<WebPushSubscription> mySubscriptions();

    WebPushSubscription addSubscriptions(String userEmail, Subscription subscription);

    String saveSub(SubscriptionDTO subscriptionDTO);

    void removeSubscriptions(String userEmail, Subscription subscription);

   void getSubscriptions(String userEmail, String name, List<UserRole> members);

}
