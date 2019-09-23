package it.eng.unipa.filesharing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.eng.unipa.filesharing.context.SecurityContext;
import it.eng.unipa.filesharing.dto.SubscriptionDTO;
import it.eng.unipa.filesharing.model.Team;
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
public class SubscriptionRegistryImpl implements SubscriptionsRegistryService {

    @Autowired SubscriptionsRegistryService subscriptionsRegistryService;
    @Autowired ConversionService conversionService;
    @Autowired SubRepository subRepository;
    @Autowired
    private ObjectMapper objectMapper;



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
    public void getSubscriptions(String userEmail, String name, List<UserRole> members) {
        PushService pushService = new PushService();
        List <WebPushSubscription> webPushSubscriptions = new ArrayList<>();
        webPushSubscriptions.addAll(subRepository.findByEmail("davide_ag@hotmail.it"));

        try {
            pushService.setPublicKey("BBYCxwATP2vVgw7mMPHJfT6bZrJP2iUV7OP_oxHzEcNFenrX66D8G34CdEmVULNg4WJXfjkeyT0AT9LwavpN8M4=");
            pushService.setPrivateKey("AKYLHgp-aV3kOys9Oy6QgxNI6OGIlOB3G6kjGvhl57j_");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        WebPushMessage message = new WebPushMessage();
        message.title = "Nuovo File";
        message.message = "Aggiunto il file " + name + " da utente " + SecurityContext.getEmail();

       // for (UserRole member: members) {
            for (int i = 0; i < webPushSubscriptions.size(); i++) {
                WebPushSubscription webPushSubscription = webPushSubscriptions.get(i);
                System.out.println(webPushSubscription.getEmail() + webPushSubscription.getEndpoint()+"");

                Notification notification = null;
                try {
                    notification = new Notification(
                            webPushSubscription.getEndpoint(),
                            webPushSubscription.getP256dh(),
                            webPushSubscription.getAuth(),
                            objectMapper.writeValueAsBytes(message));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                try {
                    pushService.send(notification);
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JoseException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    //}
}
