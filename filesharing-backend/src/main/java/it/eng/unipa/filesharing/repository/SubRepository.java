package it.eng.unipa.filesharing.repository;


import it.eng.unipa.filesharing.model.WebPushSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public  interface SubRepository extends JpaRepository<WebPushSubscription, String> /*extends MongoRepository<Team, UUID>*/{

    @Query("select w from WebPushSubscription w where w.email=:email")
    Optional<WebPushSubscription>
    SubscritionsUser(@Param("email")String email);

//  ALL SUBSCRIPTIONS TO A TEAM FOR A USER
    @Query("select w from WebPushSubscription w where w.email=:email and w.uuid=:uuid")
    Map<String, WebPushSubscription> mySubstriptions(@Param("email") String email, @Param("uuid")UUID uuid);


    Optional<WebPushSubscription> findByEmail(@Param("email")String email);
}
