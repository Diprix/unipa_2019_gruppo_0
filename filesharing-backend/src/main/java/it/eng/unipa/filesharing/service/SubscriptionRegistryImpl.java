package it.eng.unipa.filesharing.service;

import it.eng.unipa.filesharing.model.WebPushSubscription;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SubscriptionRegistryImpl implements SubscriptionsRegistryService {


    private Map<String, Map<String, WebPushSubscription>> subscriptions = new ConcurrentHashMap<>();


    @Override
    public void saveSubscription(String userEmail, WebPushSubscription subscription) {

        Map<String, WebPushSubscription> map;

        if(subscriptions.containsKey(userEmail)){
            map = subscriptions.get(userEmail);
        } else {
            map = new HashMap<>();
            subscriptions.put(userEmail, map);
        }

        map.put(subscription.getEndpoint(), subscription);

    }

    @Override
    public void deleteSubscription(String userEmail, WebPushSubscription subscription) {
        //todo
    }

    @Override
    public Collection<WebPushSubscription> getSubscriptions(String userEmal) {
        if(subscriptions.containsKey(userEmal)){
            return subscriptions.get(userEmal).values();
        } else {
            return null;
        }
    }
}
