package it.eng.unipa.filesharing.converter.entity2dto;

import it.eng.unipa.filesharing.dto.SubscriptionDTO;
import it.eng.unipa.filesharing.model.WebPushSubscription;
import org.springframework.core.convert.converter.Converter;

public class SubscriptionConvert implements Converter<WebPushSubscription, SubscriptionDTO> {

    @Override
    public SubscriptionDTO convert(WebPushSubscription webPushSubscription) {
        SubscriptionDTO subscriptionDTO= null;
        subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setEmail(webPushSubscription.getEmail());
        subscriptionDTO.getWebPushSubscription().setEndpoint(webPushSubscription.getEndpoint());
        subscriptionDTO.getWebPushSubscription().setAuth(webPushSubscription.getAuth());
        subscriptionDTO.getWebPushSubscription().setP256dh(webPushSubscription.getP256dh());

        return subscriptionDTO;
    }
}
