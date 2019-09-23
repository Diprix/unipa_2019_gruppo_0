package it.eng.unipa.filesharing.repository;

import it.eng.unipa.filesharing.model.Team;
import it.eng.unipa.filesharing.model.WebPushSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public  interface SubRepository extends JpaRepository<WebPushSubscription, Long>/*extends MongoRepository<Team, UUID>*/{

  //  @Query("select '*' from Web_Push_Subscription")
    List<WebPushSubscription> findByEmail(String email);


}
