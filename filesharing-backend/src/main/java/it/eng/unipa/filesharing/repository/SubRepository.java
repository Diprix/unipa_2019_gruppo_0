package it.eng.unipa.filesharing.repository;

import it.eng.unipa.filesharing.model.Team;
import it.eng.unipa.filesharing.model.WebPushSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public  interface SubRepository extends JpaRepository<WebPushSubscription, String>/*extends MongoRepository<Team, UUID>*/{


    @Query("select w from Team t, web_push_subscription w, user_role ur where ur.team_uuid=t.uuid and w.email=ur.email and ur.email=:email")
    List<WebPushSubscription> mySubscription(@Param("email")String email);

}
