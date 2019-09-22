package it.eng.unipa.filesharing.service;

import it.eng.unipa.filesharing.dto.SubscriptionDTO;
import it.eng.unipa.filesharing.model.WebPushSubscription;
import it.eng.unipa.filesharing.repository.SubRepository;
import nl.martijndwars.webpush.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class SubscriptionRegistryImpl implements SubscriptionsRegistryService {

    @Autowired SubscriptionsRegistryService subscriptionsRegistryService;
    @Autowired ConversionService conversionService;
    @Autowired SubRepository subRepository;


    public SubscriptionRegistryImpl(@Autowired SubRepository subRepository, @Autowired ConversionService conversionService, @Autowired SubscriptionsRegistryService subscriptionsRegistryService) {
        this.subRepository = subRepository;
        this.conversionService = conversionService;
        this.subscriptionsRegistryService = subscriptionsRegistryService;

    }

    @Override
    public List<SubscriptionDTO> mySubscription() {
        return null;
    }

    @Override
    public List<SubscriptionDTO> mySubscription(String email) {
        return null;
    }


    @Override
    public SubscriptionDTO get(UUID uuid) {
        return null;
    }

    @Override
    public WebPushSubscription addSubscriptions(String userEmail, Subscription subscription) {
        WebPushSubscription webPushSubscription= null;
        webPushSubscription.setEmail(userEmail);
        webPushSubscription.setEndpoint(subscription.endpoint);
        webPushSubscription.setAuth(subscription.keys.auth);
        webPushSubscription.setP256dh(subscription.keys.p256dh);
        return webPushSubscription;
    }

    @Override
    public SubscriptionDTO removeSubscriptions(String userEmail, Subscription subscription) {
        WebPushSubscription webPushSubscription= null;
        webPushSubscription.setEmail(userEmail);
        webPushSubscription.setEndpoint(subscription.endpoint);
        webPushSubscription.setAuth(subscription.keys.auth);
        webPushSubscription.setP256dh(subscription.keys.p256dh);
        return conversionService.convert(webPushSubscription, SubscriptionDTO.class);
    }

    @Override
    public Collection<WebPushSubscription> getSubscriptions(String userEmal) {
        return null;
    }

    @Override
    public UUID save(SubscriptionDTO subscriptionDTO) {
        WebPushSubscription webPushSubscription = null;
        webPushSubscription.setEmail(subscriptionDTO.getEmail());


        return null;
    }

}
