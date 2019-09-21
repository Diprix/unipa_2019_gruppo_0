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

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SubscriptionRegistryImpl implements SubscriptionsRegistryService {

    private Map<String, Map<String, WebPushSubscription>> subscriptions = new ConcurrentHashMap<>();
    private SubscriptionRegistryImpl subscriptionsRegistry;
    private SubRepository subRepository;

    private ConversionService conversionService;


//
//    @Override
//    public void saveSubscription(String userEmail, WebPushSubscription subscription) {
//
//        Map<String, WebPushSubscription> map;
//
//        if (subscriptions.containsKey(userEmail)) {
//            map = subscriptions.get(userEmail);
//        } else {
//            map = new HashMap<>();
//            subscriptions.put(userEmail, map);
//        }
//
//        map.put(subscription.getEndpoint(), subscription);
//
//    }


    @Override
    public  WebPushSubscription addSubscriptions(String userEmail, SubscriptionDTO subscriptionDTO) {
        WebPushSubscription webPushSubscription = null;
        if(subscriptionDTO.getEmail()!=null) {
            webPushSubscription = webPushSubscription(subscriptionDTO.);
            webPushSubscription.setEndpoint(subscriptionDTO.getNotification().getEndpoint());
            webPushSubscription.setEmail(userEmail);
            webPushSubscription.setAuth(subscriptionDTO.getNotification().getAuth());
            webPushSubscription.setAuth(subscriptionDTO.getNotification().getP256dh());

        return SubRepository.save(webPushSubscription).getEmail();
    }

    }

    @Override
    public List<SubscriptionDTO> mySubscription() {
        return SubRepository.mySubscription(SecurityContext.getEmail()).stream().map((w)->{
            return conversionService.convert(w, SubscriptionDTO.class);
        }).collect(Collectors.toList());
    }
    @Override
    public void removeSubscriptions(String userEmail, WebPushSubscription subscription) {

    }


    @Override
    public List<SubscriptionDTO> mySubstriptions() {
        return subRepository.mySubstriptions(SecurityContext.getEmail()).stream().map((t)->{
            return conversionService.convert(t, TeamDTO.class);
        }).collect(Collectors.toList());
    }
    SubscriptionDTO
    @Override
    public Collection<WebPushSubscription> getSubscriptions(String userEmal) {
        if (subscriptions.containsKey(userEmal)) {
            return subscriptions.get(userEmal).values();
        } else {
            return null;
        }
    }

    public void callPushService(UUID uuid, String name) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {

        Team team = team(uuid);
        PushService pushService = new PushService();
        try {
            pushService.setPublicKey("BBYCxwATP2vVgw7mMPHJfT6bZrJP2iUV7OP_oxHzEcNFenrX66D8G34CdEmVULNg4WJXfjkeyT0AT9LwavpN8M4=");
            pushService.setPrivateKey("AKYLHgp-aV3kOys9Oy6QgxNI6OGIlOB3G6kjGvhl57j_");

        } catch (
                NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (
                NoSuchProviderException e) {
            e.printStackTrace();
        } catch (
                InvalidKeySpecException e) {
            e.printStackTrace();
        }

        WebPushMessage message = new WebPushMessage();
        message.title = "Nuovo File";
        message.message = "Aggiunto il file " + name + " da utente " + SecurityContext.getEmail();


        List<UserRole> members = team.getMembers();
        for (UserRole member : members) {

            Collection<WebPushSubscription> subscriptions = subscriptionsRegistry.getSubscriptions(member.getOid().getEmail());

            //	for (WebPushSubscription subscription: subscriptions) {

            Notification notification = null;
//				try {
//					notification = new Notification(
//							subscription.getEndpoint(),
//							subscription.getKeys().getP256dh(),
//							subscription.getKeys().getAuth(),
//							objectMapper.writeValueAsBytes(message));
//				} catch (NoSuchAlgorithmException e) {
//					e.printStackTrace();
//				} catch (NoSuchProviderException e) {
//					e.printStackTrace();
//				} catch (InvalidKeySpecException e) {
//					e.printStackTrace();
//				} catch (JsonProcessingException e) {
//					e.printStackTrace();
//				}
//
//				try {
//					pushService.send(notification);
//				} catch (GeneralSecurityException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				} catch (JoseException e) {
//					e.printStackTrace();
//				} catch (ExecutionException e) {
//					e.printStackTrace();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//
        }
    }

    private Team team(UUID uuid) {
        return null;
    }
}