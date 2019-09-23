package it.eng.unipa.filesharing.service;

import it.eng.unipa.filesharing.dto.SubscriptionDTO;
import it.eng.unipa.filesharing.model.WebPushSubscription;
import it.eng.unipa.filesharing.repository.SubRepository;
import it.eng.unipa.filesharing.repository.TeamRepository;
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

    WebPushSubscription webPushSubscription;

    @Autowired SubscriptionsRegistryService subscriptionsRegistryService;
    @Autowired ConversionService conversionService;
    //@Autowired SubRepository subRepository;
   // @Autowired
    TeamRepository teamRepository;


    public SubscriptionRegistryImpl(@Autowired TeamRepository teamRepository, @Autowired ConversionService conversionService) {
        //this.subRepository = subRepository;
        this.teamRepository =teamRepository;
        this.conversionService = conversionService;

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
        System.out.println(userEmail + " " + subscription);
        //WebPushSubscription webPushSubscription= null;
        webPushSubscription.setEmail(userEmail);
        webPushSubscription.setEndpoint(subscription.endpoint);
        webPushSubscription.setAuth(subscription.keys.auth);
        webPushSubscription.setP256dh(subscription.keys.p256dh);
        return webPushSubscription;
    }

    @Override
    public SubscriptionDTO removeSubscriptions(String userEmail, Subscription subscription) {

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
        webPushSubscription.setEmail(subscriptionDTO.getEmail());


        return null;
    }

}
