package it.eng.unipa.filesharing.container;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.eng.unipa.filesharing.context.SecurityContext;
import it.eng.unipa.filesharing.dto.SubscriptionDTO;
import it.eng.unipa.filesharing.dto.TeamDTO;
import it.eng.unipa.filesharing.model.WebPushSubscription;
import it.eng.unipa.filesharing.service.SubscriptionsRegistryService;
import it.eng.unipa.filesharing.service.TeamService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import nl.martijndwars.webpush.Subscription;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Notification;

import java.security.Security;
import java.util.List;
import java.util.UUID;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import static it.eng.unipa.filesharing.context.SecurityContext.getEmail;

@RestController
public class NotificationController {
    private static final String PUBLIC_KEY = "BAPGG2IY3Vn48d_H8QNuVLRErkBI0L7oDOOCAMUBqYMTMTzukaIAuB5OOcmkdeRICcyQocEwD-oxVc81YXXZPRY";
    private static final String PRIVATE_KEY = "A7xDGuxMZ4ufflcAhBW23xpoWZNOLwM4Rw2wXjP0y6M";
    private static PushService pushService = new PushService();
    private Subscription subscription= new Subscription();
    private SubscriptionsRegistryService subscriptionsRegistryService;
    WebPushSubscription webPushSubscription =new WebPushSubscription();

    public NotificationController(@Autowired SubscriptionsRegistryService subscriptionsRegistryService) {
        this.subscriptionsRegistryService = subscriptionsRegistryService;
    }

    // METODO DI REGISTRAZIONE SOTTOSCRIZIONE
    @PostMapping("/subscribe")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addSubscribe(@RequestBody Subscription subscription){
        subscriptionsRegistryService.addSubscriptions(SecurityContext.getEmail(), subscription);
        System.out.println("Sottoscrizione registrata per " + getEmail());
    }

    // METODO DI RIMOZIONE SOTTOSCRIZIONE
    @PostMapping("/unsubscribe")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeSubscribe(@RequestBody Subscription subscription) {
        subscriptionsRegistryService.removeSubscriptions(getEmail(), subscription);
         System.out.println(">>** Sottoscrizione cancellata");
    }

    // METODO CHE CONTATTA IL PUSH SERVICE IL QUALE DESTINA IL MESSAGGIO AL CLIENT CORRETTO GRAZIE ALL'ENDPOINT
    @RequestMapping("/notification/send")
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
