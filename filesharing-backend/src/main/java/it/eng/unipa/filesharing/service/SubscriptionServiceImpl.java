package it.eng.unipa.filesharing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.eng.unipa.filesharing.context.SecurityContext;
import it.eng.unipa.filesharing.converter.entity2dto.SubscriptionConvert;
import it.eng.unipa.filesharing.dto.SubscriptionDTO;
import it.eng.unipa.filesharing.model.UserRole;
import it.eng.unipa.filesharing.model.WebPushMessage;
import it.eng.unipa.filesharing.model.WebPushSubscription;
import it.eng.unipa.filesharing.repository.SubRepository;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class SubscriptionServiceImpl implements SubscriptionService {

    private ConversionService conversionService;
    private SubRepository subRepository;
    @Autowired
//    private SubscriptionConvert subscriptionConvert;

   // private Map<String, WebPushSubscription> subscriptions = new ConcurrentHashMap<>();


    public SubscriptionServiceImpl(@Autowired SubRepository subRepository, @Autowired ConversionService conversionService) {
        this.subRepository = subRepository;
        this.conversionService = conversionService;
    }

//    @Override
//    public List<SubscriptionDTO> mySubscriptions(String email, UUID uuid) {
//         return subRepository.mySubstriptions(SecurityContext.getEmail(), uuid).put((w) -> {
//            return conversionService.convert(w, SubscriptionDTO.class);
//        }).collect(Collectors.toList());
//    }


    @Override
    public List<SubscriptionDTO> mySubscriptionsDTO(String email, UUID uuid) {
        return null;
    }

    @Override
    public List<WebPushSubscription> mySubscriptions(String email) {
        subRepository.findAll();
        return mySubscriptions(email);
    }


    @Override
    public WebPushSubscription addSubscriptions(String userEmail, Subscription subscription, UUID uuid) {
        WebPushSubscription webPushSubscription = new WebPushSubscription(userEmail, subscription.keys.auth, subscription.endpoint, subscription.keys.p256dh, uuid);
        System.out.println(webPushSubscription.toString());
        return subRepository.save(webPushSubscription);
    }

    @Override
    public String saveSub(SubscriptionDTO subscriptionDTO, UUID uuid) {
        WebPushSubscription webPushSubscription = new WebPushSubscription(SecurityContext.getEmail(),
                subscriptionDTO.getEndpoint(), subscriptionDTO.getAuth(), subscriptionDTO.getP256dh(), uuid);
        System.out.println(webPushSubscription.toString());
        return subRepository.save(webPushSubscription).getEmail();
    }


    @Override
    public void removeSubscriptions(WebPushSubscription webPushSubscription) {
        subRepository.delete(webPushSubscription);
        System.out.println("Sottoscizione Cancellata");
    }

    @Override
    public Notification getSubscriptions(String userEmail, String endPoint, String nameFile) {
        return null;
    }

    @Override
    public void sendPushMessage(String email,WebPushSubscription webPushSubscription, String nameFile, UUID uuid) throws NoSuchProviderException, InvalidKeySpecException,
            JsonProcessingException, JoseException, IOException, GeneralSecurityException, ExecutionException, InterruptedException, NoSuchAlgorithmException {

        WebPushMessage message = new WebPushMessage();
        PushService pushService = new PushService();
        ObjectMapper objectMapper = new ObjectMapper();

        message.title = "Nuovo File";
        message.message = "Aggiunto il file " + nameFile + " da utente " +
                SecurityContext.getEmail();

        Notification notification = null;
        notification = new Notification(
                webPushSubscription.getEndpoint(),
                webPushSubscription.getP256dh(),
                webPushSubscription.getAuth(),
                objectMapper.writeValueAsBytes(message));

        pushService.send(notification);

    }

    @Override
    public SubscriptionDTO tree() {
        return null;
    }
}
