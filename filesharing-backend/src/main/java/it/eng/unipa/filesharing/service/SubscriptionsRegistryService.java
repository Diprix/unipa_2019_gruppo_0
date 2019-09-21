package it.eng.unipa.filesharing.service;

import it.eng.unipa.filesharing.dto.SubscriptionDTO;
import it.eng.unipa.filesharing.model.WebPushSubscription;

import java.util.Collection;
import java.util.List;

public interface SubscriptionsRegistryService {

    List<SubscriptionDTO> mySubscription();

    public WebPushSubscription addSubscriptions(String userEmail, SubscriptionDTO subscriptionDTO);
    public void removeSubscriptions(String userEmail, SubscriptionDTO subscriptionDTO);

    //
    public Collection<WebPushSubscription> getSubscriptions(String userEmal);

}
