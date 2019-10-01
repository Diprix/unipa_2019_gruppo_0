package it.eng.unipa.filesharing.container;
import it.eng.unipa.filesharing.context.SecurityContext;
import it.eng.unipa.filesharing.dto.SubscriptionDTO;
import it.eng.unipa.filesharing.model.WebPushSubscription;
import it.eng.unipa.filesharing.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import nl.martijndwars.webpush.Subscription;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Notification;

import java.security.Security;
import java.util.UUID;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import static it.eng.unipa.filesharing.context.SecurityContext.getEmail;

@RestController
public class NotificationController {
    private static final String PUBLIC_KEY = "BAPGG2IY3Vn48d_H8QNuVLRErkBI0L7oDOOCAMUBqYMTMTzukaIAuB5OOcmkdeRICcyQocEwD-oxVc81YXXZPRY";
    private static final String PRIVATE_KEY = "A7xDGuxMZ4ufflcAhBW23xpoWZNOLwM4Rw2wXjP0y6M";
    private static final String SUBJECT = "Foobarbaz";
    private static final String PAYLOAD = "My fancy message";
    private static PushService pushService = new PushService();
    private Subscription subscription= new Subscription();

       //CREO IL SERVIZIO CHE MI GESTISCE LE OPERAZIONI DI SOTTOSCRIZIONE.
    private SubscriptionService subscriptionService;
    WebPushSubscription webPushSubscription =new WebPushSubscription();

    public NotificationController(@Autowired SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }


//    @GetMapping("")
//    public ResponseEntity<List<SubscriptionDTO>> mySubscription(){
//        return new ResponseEntity<List<SubscriptionDTO>>(this.subscriptionService.mySubscription(getEmail()),HttpStatus.OK);
//    }
    // METODO DI SOTTOSCRIZIONE
    @PostMapping("/subscribe")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SubscriptionDTO saveSub(@RequestBody Subscription  subscription){
        System.out.println("Sottoscrizione registrata per " + getEmail());
        SubscriptionDTO dto = subscriptionService.saveSub(subscription);
        System.out.println("Sottoscrizione registrata per " + getEmail());
        return dto;
    }

    @PostMapping("/subscribe2")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addSubscribe(@RequestBody Subscription subscription){
        subscriptionService.addSubscriptions(SecurityContext.getEmail(), subscription, UUID.randomUUID());
        System.out.println("Sottoscrizione registrata per " + getEmail());
    }

    // METODO DI RIMOZIONE SOTTOSCRIZIONE
    @PostMapping("/unsubscribe")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeSubscribe(@RequestBody Subscription subscription) {

    subscriptionService.removeSubscriptions(subscription);
         System.out.println(">>** Sottoscrizione cancellata");
    }

    // METODO CHE CONTATTA IL PUSH SERVICE IL QUALE DESTINA IL MESSAGGIO AL CLIENT CORRETTO GRAZIE ALL'ENDPOINT
    @RequestMapping("/send")
    public String send(@RequestParam("subscriptionJson") String subscriptionJson) {
        Security.addProvider(new BouncyCastleProvider());
        try {
            PushService pushService = new PushService(PUBLIC_KEY, PRIVATE_KEY, SUBJECT);
            pushService.setPublicKey("BBYCxwATP2vVgw7mMPHJfT6bZrJP2iUV7OP_oxHzEcNFenrX66D8G34CdEmVULNg4WJXfjkeyT0AT9LwavpN8M4=");
            pushService.setPrivateKey("AKYLHgp-aV3kOys9Oy6QgxNI6OGIlOB3G6kjGvhl57j_");
            Subscription subscription = new Gson().fromJson(subscriptionJson, Subscription.class);
            Notification notification = new Notification(subscription, PAYLOAD);
            HttpResponse httpResponse = pushService.send(notification);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            return String.valueOf(statusCode);
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }
}
