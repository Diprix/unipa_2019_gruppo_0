package it.eng.unipa.filesharing;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.jose4j.lang.JoseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import it.eng.unipa.filesharing.dto.BucketDTO;
import it.eng.unipa.filesharing.dto.MembershipDTO;
import it.eng.unipa.filesharing.dto.ResourceDTO;
import it.eng.unipa.filesharing.dto.TeamDTO;
import it.eng.unipa.filesharing.service.TeamService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeamServiceTest {
	
	private static Logger LOGGER = LoggerFactory.getLogger(TeamServiceTest.class);
	
	@Autowired
	TeamService teamService;
	
	@Test
	@Transactional
	@Rollback(false)
	public void test() {
		
		String bucketName = "miobucket";
		
		TeamDTO teamDTO = new TeamDTO();
		teamDTO.setName("Team test");
		teamDTO.setDescription("Team test description");
		UUID uuid = teamService.save(teamDTO);
		LOGGER.info("UUID {}",uuid);
		
		
		
		
		String otherEmail1 = "cristian.amato@eng.it";
		String otherEmail2 = "luigi.graci@eng.it";
		
		
		teamService.inviteMember(uuid, otherEmail1, true);
		teamService.inviteMember(uuid, otherEmail2, true);
		
		
		teamService.removeMember(uuid, otherEmail2);
		
		
		
		teamService.addBucket(uuid, new BucketDTO(bucketName, "bucket description","filesystem"));
		
		TeamDTO teamDTO2 = teamService.get(uuid);
		LOGGER.info("TEAM {}",teamDTO2);
		
		ResourceDTO r = teamService.tree(uuid, bucketName);
		
		LOGGER.info("TEAM {}",r);
		
		teamService.addMembership(uuid, bucketName, new MembershipDTO(otherEmail1,true,true));

		ResourceDTO addContent = null;
		try {
			addContent = teamService.addContent(uuid,bucketName,null,"testina.txt","ciao".getBytes());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (JoseException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ResourceDTO content = teamService.getContent(uuid, bucketName, addContent.getUniqueKey());
		
		System.out.println(content);
		
		
	}

}
