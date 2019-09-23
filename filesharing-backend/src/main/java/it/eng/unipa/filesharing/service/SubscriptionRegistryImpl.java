package it.eng.unipa.filesharing.service;

import it.eng.unipa.filesharing.context.SecurityContext;
import it.eng.unipa.filesharing.dto.SubscriptionDTO;
import it.eng.unipa.filesharing.model.Team;
import it.eng.unipa.filesharing.model.WebPushSubscription;
import it.eng.unipa.filesharing.repository.SubRepository;
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
        subRepository.findAll();
        return mySubscriptions();
    }


    @Override
    public WebPushSubscription addSubscriptions(String userEmail, Subscription subscription) {
        WebPushSubscription webPushSubscription = new WebPushSubscription(userEmail, subscription.keys.auth, subscription.endpoint, subscription.keys.p256dh);
        System.out.println(webPushSubscription.toString());
        return subRepository.save(webPushSubscription);
    }

    @Override
    public String saveSub(SubscriptionDTO subscriptionDTO) {
        WebPushSubscription webPushSubscription = new WebPushSubscription(SecurityContext.getEmail(), subscriptionDTO.getWebPushSubscription().getEndpoint(),
                subscriptionDTO.getWebPushSubscription().getAuth(),subscriptionDTO.getWebPushSubscription().getP256dh());
                System.out.println(webPushSubscription.toString());
        return subRepository.save(webPushSubscription).getEmail();
    }


    @Override
    public void removeSubscriptions(String userEmail, Subscription subscription) {
       List <WebPushSubscription> webPushSubscription = new ArrayList<>();
        webPushSubscription.addAll(subRepository.findByEmail(userEmail));
           System.out.println("Nessuna Sottoscizione Trovata per l'utente");
           subRepository.delete(webPushSubscription.iterator().next());
           System.out.println("Sottoscizione Cancellata");
       }

    @Override
    public Collection<WebPushSubscription> getSubscriptions(String userEmal) {
        return null;
    }



}
