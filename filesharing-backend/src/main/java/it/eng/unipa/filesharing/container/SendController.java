package it.eng.unipa.filesharing.container;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.eng.unipa.filesharing.model.WebPushMessage;
import it.eng.unipa.filesharing.model.WebPushSubscription;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import nl.martijndwars.webpush.Subscription;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Notification;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

@RestController
public class SendController {
    private static final String PUBLIC_KEY = "BAPGG2IY3Vn48d_H8QNuVLRErkBI0L7oDOOCAMUBqYMTMTzukaIAuB5OOcmkdeRICcyQocEwD-oxVc81YXXZPRY";
    private static final String PRIVATE_KEY = "A7xDGuxMZ4ufflcAhBW23xpoWZNOLwM4Rw2wXjP0y6M";
    private static final String SUBJECT = "Foobarbaz";
    private static final String PAYLOAD = "My fancy message";
    private static PushService pushService = new PushService();

    private Map<String, WebPushSubscription> subscriptions = new ConcurrentHashMap<>();

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/subscribe")
    public void subscribe(WebPushSubscription subscription) {

        subscriptions.put(subscription.getNotificationEndPoint(), subscription);
    }

    @PostMapping("/unsubscribe")
    public void unsubscribe(WebPushSubscription subscription) {

        subscriptions.remove(subscription.getNotificationEndPoint());
    }

    @PostMapping("/notify-all")
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