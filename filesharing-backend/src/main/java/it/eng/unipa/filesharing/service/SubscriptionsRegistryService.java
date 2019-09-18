package it.eng.unipa.filesharing.service;

import it.eng.unipa.filesharing.model.WebPushSubscription;

import java.util.Collection;

public interface SubscriptionsRegistryService {

    public void saveSubscription(String userEmail, WebPushSubscription subscription);
    public void deleteSubscription(String userEmail, WebPushSubscription subscription);
    public Collection<WebPushSubscription> getSubscriptions(String userEmal);

}
