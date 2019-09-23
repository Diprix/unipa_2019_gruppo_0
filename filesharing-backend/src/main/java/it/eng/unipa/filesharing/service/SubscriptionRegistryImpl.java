package it.eng.unipa.filesharing.service;

import it.eng.unipa.filesharing.context.SecurityContext;
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
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class SubscriptionRegistryImpl implements SubscriptionsRegistryService {

    @Autowired SubscriptionsRegistryService subscriptionsRegistryService;
    @Autowired ConversionService conversionService;
    @Autowired SubRepository subRepository;



    public SubscriptionRegistryImpl(@Autowired SubRepository subRepository, @Autowired ConversionService conversionService) {
        this.subRepository = subRepository;
        //this.teamRepository =teamRepository;
        this.conversionService = conversionService;

    }

    // Collection<WebPushSubscription> subscriptions = subscriptionsRegistryService.getSubscriptions(member.getOid().getEmail());
    @Override
    public List<WebPushSubscription> mySubscriptions() {
        return null;
    }

    @Override
    public WebPushSubscription addSubscriptions(String userEmail, Subscription subscription) {
        WebPushSubscription webPushSubscription = new WebPushSubscription(userEmail, subscription.keys.auth, subscription.endpoint, subscription.keys.p256dh);
        System.out.println(webPushSubscription.toString());
        return webPushSubscription;
    }


    @Override
    public void removeSubscriptions(String userEmail, Subscription subscription) {
//        WebPushSubscription webPushSubscription= null;
//        webPushSubscription.setEmail(userEmail);
//        webPushSubscription.setEndpoint(subscription.endpoint);
//        webPushSubscription.setAuth(subscription.keys.auth);
//        webPushSubscription.setP256dh(subscription.keys.p256dh);
//        return conversionService.convert(webPushSubscription, SubscriptionDTO.class);
        System.out.println("Cordiali Saluti");
    }

    @Override
    public Collection<WebPushSubscription> getSubscriptions(String userEmal) {
        return null;
    }



}
