package it.eng.unipa.filesharing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.eng.unipa.filesharing.context.SecurityContext;
import it.eng.unipa.filesharing.converter.entity2dto.SubscriptionConvert;
import it.eng.unipa.filesharing.dto.SubscriptionDTO;
import it.eng.unipa.filesharing.model.PushSelector;
import it.eng.unipa.filesharing.model.UserRole;
import it.eng.unipa.filesharing.model.WebPushMessage;
import it.eng.unipa.filesharing.model.WebPushSubscription;
import it.eng.unipa.filesharing.repository.SubRepository;
import it.eng.unipa.filesharing.service.exception.NotFoundException;
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


    public SubscriptionServiceImpl(@Autowired SubRepository subRepository, @Autowired ConversionService conversionService) {
        this.subRepository = subRepository;
        this.conversionService = conversionService;
    }

    /// SAVE SUBSCRIPTION FOR USERMAIL
    @Override
    public SubscriptionDTO saveSub(Subscription subscription) {
        String email = SecurityContext.getEmail();
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
//        Optional<WebPushSubscription> findById = subRepository.findById(email);
//        if(!findById.isPresent()){

            WebPushSubscription webPushSubscription = new WebPushSubscription(email, subscription.endpoint, subscription.keys.auth, subscription.keys.p256dh);

            subRepository.save(webPushSubscription);

            subscriptionDTO.setEmail(email);
            subscriptionDTO.setEndpoint(subscription.endpoint);
            subscriptionDTO.setAuth(subscription.keys.auth);
            subscriptionDTO.setP256dh(subscription.keys.p256dh);
//
//        }else{
//            subscriptionDTO.setEmail(findById.get().getEmail());
//        }

        return subscriptionDTO;
    }


    @Override
    public List<SubscriptionDTO> mySubscriptionsDTO(String email, UUID uuid) {
        return null;
    }

    @Override
    public List<WebPushSubscription> mySubscriptions(String email) {
        subRepository.findAll();
        return mySubscriptions(email);
    }

//    @Override
//    public void setPushAction(PushSelector pushSelector) {
//        Long id= Long.valueOf(1);
//        Optional<WebPushSubscription> findById = subRepository.findById(id);
//        if(findById.isPresent() ) {
////            Team team = findById.get();
//            System.out.println("Trovata sottoscrizione");
////            boolean esito = team.removeBucket(SecurityContext.getEmail(), name);
//        }
//
//    }


    @Override
    public WebPushSubscription addSubscriptions(String userEmail, Subscription subscription, UUID uuid) {
        WebPushSubscription webPushSubscription = new WebPushSubscription(userEmail, subscription.keys.auth, subscription.endpoint, subscription.keys.p256dh, uuid);
        System.out.println(webPushSubscription.toString());
        return subRepository.save(webPushSubscription);
    }



    @Override
    public void setPushAction(PushSelector pushSelector) {
        Long id= Long.valueOf(1);
        Optional<WebPushSubscription> findByEmail = subRepository.findByEmail(SecurityContext.getEmail());
        if(findByEmail.isPresent() ) {
//            Team team = findById.get();
            System.out.println("Trovata sottoscrizione");
//            boolean esito = team.removeBucket(SecurityContext.getEmail(), name);
        }

    }

    private WebPushSubscription webPushSubscription(Subscription subscription) {
        WebPushSubscription webPushSubscription = subRepository.findById(SecurityContext.getEmail()).orElseThrow(() -> new NotFoundException(SecurityContext.getEmail()));
        return webPushSubscription;
    }

    @Override
    public void removeSubscriptions(Subscription subscription) {
        String email = SecurityContext.getEmail();
        WebPushSubscription ws =new WebPushSubscription(email, subscription.keys.auth, subscription.endpoint, subscription.keys.p256dh);
        subRepository.findByEmail(email);
       // if(!findByEmail.isPresent()){

        //}
 //        List <WebPushSubscription>mySub = new ArrayList<>();
//        mySub.addAll(subRepository.findByEmail(email));
//        subRepository.delete(new C(email, subscription.endpoint, subscription.keys.auth, subscription.keys.p256dh));
//        Optional<WebPushSubscription> webPushSubscription= subRepository.findByEmail(email);
//        if(webPushSubscription.isPresent() ){
//            String end = webPushSubscription.get().getEndpoint();
////            boolean esito = team.removeBucket(SecurityContext.getEmail(), name);
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
