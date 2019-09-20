package it.eng.unipa.filesharing.service;

import java.util.List;
import java.util.UUID;

import it.eng.unipa.filesharing.dto.*;

public interface TeamService {
	
	List<TeamDTO> myTeams();
	//List<TeamDTO> mySubscription();

	UUID save(TeamDTO team);
	
	TeamDTO get(UUID uuid);

	void delete(UUID uuid, Boolean recursive);
	
	void acceptInvite(UUID uuid);

	void rejectInvite(UUID uuid);

	void inviteMember(UUID uuid, String otherEmail, boolean admin);
	
	void addMembership(UUID uuid,String bucketName,MembershipDTO membershipDTO);
	
	void removeMember(UUID uuid, String otherEmail);
	
	void addBucket(UUID uuid,BucketDTO bucketDTO);

	SubscriptionDTO addSubscription(String mail, SubscriptionDTO subscriptionDTO);
	
	void removeBucket(UUID uuid, String bucketName);

	List<BucketTypeDTO> bucketTypeSupport();

	ResourceDTO tree(UUID uuid, String name);

	ResourceDTO addContent(UUID uuid, String bucketName, String parentUniqueId, String name, byte[] content);

	ResourceDTO addSubscription(UUID uuid, String bucketName, String parentUniqueId, String name, byte[] content);

	ResourceDTO addFolder(UUID uuid, String bucketName, String parentUniqueId, String name);

	ResourceDTO getContent(UUID uuid, String bucketName, String uniqueId);
	
	
}
