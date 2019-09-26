package it.eng.unipa.filesharing.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import it.eng.unipa.filesharing.dto.*;
import it.eng.unipa.filesharing.model.WebPushSubscription;
import org.jose4j.lang.JoseException;

public interface TeamService {
	
	List<TeamDTO> myTeams();

	UUID save(TeamDTO team);
	
	TeamDTO get(UUID uuid);

	void delete(UUID uuid, Boolean recursive);
	
	void acceptInvite(UUID uuid);

	void rejectInvite(UUID uuid);

	void inviteMember(UUID uuid, String otherEmail, boolean admin);
	
	void addMembership(UUID uuid,String bucketName,MembershipDTO membershipDTO);
	
	void removeMember(UUID uuid, String otherEmail);
	
	void addBucket(UUID uuid,BucketDTO bucketDTO);
	
	void removeBucket(UUID uuid, String bucketName);

	List<BucketTypeDTO> bucketTypeSupport();

	ResourceDTO tree(UUID uuid, String name);

	ResourceDTO addContent(UUID uuid, String bucketName, String parentUniqueId, String name, byte[] content) throws InterruptedException, GeneralSecurityException, JoseException, ExecutionException, IOException;

	ResourceDTO addFolder(UUID uuid, String bucketName, String parentUniqueId, String name);

	ResourceDTO getContent(UUID uuid, String bucketName, String uniqueId);

	
}
