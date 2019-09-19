package it.eng.unipa.filesharing.repository;

import java.util.List;
import java.util.UUID;

import it.eng.unipa.filesharing.model.MemberSubscription;
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
//
//	@Query("select t from Team t, MemberSubscription m where t.email = m.email and m.oid.email=:email")
//	List<MemberSubscription> mySubscritption(@Param("email")String email);
//

}
