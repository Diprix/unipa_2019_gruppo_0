package it.eng.unipa.filesharing.repository;

import java.util.List;
import java.util.UUID;


import it.eng.unipa.filesharing.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
/*import org.springframework.data.mongodb.repository.MongoRepository;*/
import org.springframework.stereotype.Repository;

import it.eng.unipa.filesharing.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID>/*extends MongoRepository<Team, UUID>*/{

	
	@Query("select t from Team t join t.members m where m.oid.email=:email")
	List<Team> myTeams(@Param("email")String email);
////
//	@Query("select m from membership m, member_subscription ms where m.email= ms.email and m.email=:email")
//	List<MemberSubscription> mySubscription(@Param("email")String email);


}
