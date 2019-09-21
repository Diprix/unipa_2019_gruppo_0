package it.eng.unipa.filesharing.service;

import it.eng.unipa.filesharing.context.SecurityContext;
import it.eng.unipa.filesharing.dto.SubscriptionDTO;
import it.eng.unipa.filesharing.model.Team;
import it.eng.unipa.filesharing.model.UserRole;
import it.eng.unipa.filesharing.model.WebPushMessage;
import it.eng.unipa.filesharing.model.WebPushSubscription;
import it.eng.unipa.filesharing.repository.SubRepository;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class SubscriptionRegistryImpl implements SubscriptionsRegistryService {

    private SubscriptionsRegistryService subscriptionsRegistryService;
    private SubRepository subRepository;
    private ConversionService conversionService; //Convert JPA


    @Override
    public SubscriptionDTO addSubscriptions(String userEmail, WebPushSubscription webPushSubscription) {
        SubscriptionDTO subscriptionDTO = null;
            subscriptionDTO = new SubscriptionDTO(userEmail, webPushSubscription);
            return subscriptionDTO;

    }

    @Override
    public SubscriptionDTO removeSubscriptions(String userEmail, WebPushSubscription webPushSubscription) {
        return null;
    }

    @Override
    public Collection<WebPushSubscription> getSubscriptions(String userEmal) {
        return null;
    }

//    @Override
//    public List<SubscriptionDTO> mySubscription(String email) {/**/
//        return mySubscription(new email.stream().map((w)->{
//            return conversionService.convert(w, SubscriptionDTO.class);
//        }).collect(Collectors.toList());
//    }
public List<UserRole> members = team.getMembers();
		for (UserRole member: members) {

        Collection<WebPushSubscription> subscriptions = subscriptionsRegistry.getSubscriptions(member.getOid().getEmail());

        for (WebPushSubscription subscription: subscriptions) {

    @Override
    public UUID save(SubscriptionDTO webPushSubscription) {
        return null;
    }

    @Override
    public SubscriptionDTO get(UUID uuid) {
        return null;
    }


}