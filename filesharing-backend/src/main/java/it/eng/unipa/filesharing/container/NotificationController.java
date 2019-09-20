package it.eng.unipa.filesharing.container;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.eng.unipa.filesharing.dto.SubscriptionDTO;
import it.eng.unipa.filesharing.model.WebPushSubscription;
import it.eng.unipa.filesharing.service.SubscriptionsRegistryService;
import it.eng.unipa.filesharing.service.TeamService;
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

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import static it.eng.unipa.filesharing.context.SecurityContext.getEmail;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    private static final String PUBLIC_KEY = "BAPGG2IY3Vn48d_H8QNuVLRErkBI0L7oDOOCAMUBqYMTMTzukaIAuB5OOcmkdeRICcyQocEwD-oxVc81YXXZPRY";
    private static final String PRIVATE_KEY = "A7xDGuxMZ4ufflcAhBW23xpoWZNOLwM4Rw2wXjP0y6M";
    private static final String SUBJECT = "Foobarbaz";
    private static final String PAYLOAD = "My fancy message";
    private static PushService pushService = new PushService();
    private TeamService teamService;


    @Autowired
    private SubscriptionsRegistryService subscriptionsRegistry;

    @Autowired
    private ObjectMapper objectMapper;



    public NotificationController(@Autowired TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/subscribe")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SubscriptionDTO addSub(@PathVariable("email") String mail,@RequestBody SubscriptionDTO subscription){
        return teamService.addSubscription(mail,subscription);
    }
    public void subscribe(@RequestBody WebPushSubscription subscription) {

        System.out.println(">>## " + getEmail());

        subscriptionsRegistry.saveSubscription(getEmail(), subscription);

        System.out.println(subscription.getEndpoint());
    }

    @PostMapping("/unsubscribe")
    @ResponseStatus(value = HttpStatus.OK)
    public void unsubscribe(WebPushSubscription subscription) {

        System.out.println(">>** Sottoscrizione cancellata");
        subscriptionsRegistry.deleteSubscription(getEmail(), subscription);
        System.out.println(subscription.getEndpoint());
    }

 /*   @PostMapping("/notify-all")
    public WebPushMessage notifyAll(@RequestBody WebPushMessage message) throws GeneralSecurityException, IOException, JoseException, ExecutionException, InterruptedException {

        for (WebPushSubscription subscription: subscriptions.values()) {

            Notification notification = new Notification(
                    subscription.getNotificationEndPoint(),
                    subscription.getPublicKey(),
                    subscription.getAuth(),
                    objectMapper.writeValueAsBytes(message));

            pushService.send(notification);
        }

        return message;
    }
*/
    @RequestMapping("/send")
    public String send(@RequestParam("subscriptionJson") String subscriptionJson) {
        Security.addProvider(new BouncyCastleProvider());

        try {
            PushService pushService = new PushService(PUBLIC_KEY, PRIVATE_KEY, SUBJECT);
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