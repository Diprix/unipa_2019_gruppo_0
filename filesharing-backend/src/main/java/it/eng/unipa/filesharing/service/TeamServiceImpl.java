package it.eng.unipa.filesharing.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.eng.unipa.filesharing.dto.*;
import it.eng.unipa.filesharing.model.*;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.eng.unipa.filesharing.context.SecurityContext;
import it.eng.unipa.filesharing.repository.TeamRepository;
import it.eng.unipa.filesharing.resource.BucketResource;
import it.eng.unipa.filesharing.resource.BucketType;
import it.eng.unipa.filesharing.resource.ContentResource;
import it.eng.unipa.filesharing.resource.FolderResource;
import it.eng.unipa.filesharing.service.exception.TeamNotFoundException;


@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class TeamServiceImpl implements TeamService{

	@Autowired
	private SubscriptionsRegistryService subscriptionsRegistry;

	@Autowired
	private ObjectMapper objectMapper;



	private TeamRepository teamRepository;

	private ConversionService conversionService;

	private List<BucketType> allBucketType;

	public TeamServiceImpl(/*@Autowired ResourceRepository resourceRepository,*/@Autowired TeamRepository teamRepository,@Autowired ConversionService conversionService,@Autowired List<BucketType> allBucketType) {
		this.teamRepository = teamRepository;
		this.conversionService = conversionService;
		this.allBucketType = allBucketType;
	}

	@Override
	public TeamDTO get(UUID uuid) {
		Team team = team(uuid);

		return conversionService.convert(team, TeamDTO.class);

	}

	@Override
	public List<TeamDTO> myTeams() {
		return teamRepository.myTeams(SecurityContext.getEmail()).stream().map((t)->{
			return conversionService.convert(t, TeamDTO.class);
		}).collect(Collectors.toList());
	}


	@Override
	public UUID save(TeamDTO teamDTO) {
		Team team = null;
		if(teamDTO.getUuid()!=null) {
			team = team(teamDTO.getUuid());
			team.setName(teamDTO.getName());
			team.setDescription(teamDTO.getDescription());
		}else {
			team = new Team(SecurityContext.getEmail(), teamDTO.getName(), teamDTO.getDescription());
			for(UserRoleDTO x: teamDTO.getMembers()) {

				inviteNotification("Sei stato invitato in un Team",SecurityContext.getLongName() + " ti ha invitato a far parte del team " + teamDTO.getName(), x.getEmail());


				team.inviteMember(SecurityContext.getEmail(), x.getEmail(), x.isAdmin());

			}
		}

		return teamRepository.save(team).getUuid();
	}

	@Override
	public void delete(UUID uuid, Boolean recursive) {
		Team team = team(uuid);
		teamRepository.delete(team);
	}

	@Override
	public void inviteMember(UUID uuid, String otherEmail,boolean admin) {
		Team team = team(uuid);
		team.inviteMember(SecurityContext.getEmail(), otherEmail, admin);
	}

	@Override
	public void acceptInvite(UUID uuid) {
		Team team = team(uuid);
		boolean esito = team.acceptInvite(SecurityContext.getEmail(),SecurityContext.getLongName());
		team.getBuckets().forEach(x-> x.addMembership(SecurityContext.getEmail(), true, true));
		sendNotification("Invito accettato",SecurityContext.getLongName() + " si è unito al tuo team",uuid);
		teamRepository.save(team);
	}

	@Override
	public void rejectInvite(UUID uuid) {
		Team team = team(uuid);
		boolean esito = team.rejectInvite(SecurityContext.getEmail());

		sendNotification("Invito Rifiutato",SecurityContext.getLongName() + " ha rifiutato l'invito",uuid);
	}

	@Override
	public void removeMember(UUID uuid, String otherEmail) {
		Optional<Team> findById = teamRepository.findById(uuid);
		if(findById.isPresent() ){
			Team team = findById.get();
			boolean esito = team.removeMember(SecurityContext.getEmail(), otherEmail);
		}
	}

	@Override
	public void addBucket(UUID uuid,BucketDTO bucketDTO) {
		Optional<Team> findById = teamRepository.findById(uuid);
		if(findById.isPresent() ){
			Team team = findById.get();

			BucketType bucketType = contains(bucketDTO.getBucketType());

			boolean esito = team.addBucket(bucketType,SecurityContext.getEmail(), bucketDTO.getName(),bucketDTO.getDescription());

		}

	}


	@Override
	public void removeBucket(UUID uuid, String name) {
		Optional<Team> findById = teamRepository.findById(uuid);
		if(findById.isPresent() ){
			Team team = findById.get();
			boolean esito = team.removeBucket(SecurityContext.getEmail(), name);
		}

	}

	@Override
	public List<BucketTypeDTO> bucketTypeSupport(){
		return this.allBucketType.stream().map((bt)->new BucketTypeDTO(bt.getName(),bt.getDescription())).collect(Collectors.toList());
	}

	private BucketType contains(String bucketTypeName) {
		return this.allBucketType.stream().filter((b)->b.getName().equals(bucketTypeName)).findFirst().orElseGet(null);
	}

	@Override
	public ResourceDTO tree(UUID uuid, String bucketName) {
		Team team = team(uuid);
		Bucket bucket = team.bucket(bucketName);
		return (ResourceDTO)conversionService.convert(bucket.getBucketResource(),TypeDescriptor.valueOf(BucketResource.class), TypeDescriptor.valueOf(ResourceDTO.class));
	}

	private Team team(UUID uuid) {
		Team team = teamRepository.findById(uuid).orElseThrow(() -> new TeamNotFoundException(uuid));
		return team;
	}

	@Override
	public void addMembership(UUID uuid, String bucketName, MembershipDTO membershipDTO) {
		Team team = team(uuid);
		team.addMembership(SecurityContext.getEmail(), bucketName, membershipDTO.getEmail(), membershipDTO.isPermissionCreate(), membershipDTO.isPermissionDelete());
	}

	@Override
	public ResourceDTO addContent(UUID uuid, String bucketName,String parentUniqueId,String name,byte[] content) {
		Team team = team(uuid);
		ContentResource contentResource = team.addContent(bucketName, parentUniqueId, SecurityContext.getEmail(), name, content);

		sendNotification("Nuovo File","Aggiunto il file " + name + " da " + SecurityContext.getLongName(),uuid);


		return conversionService.convert(contentResource, ResourceDTO.class);
	}

	@Override
	public ResourceDTO addFolder(UUID uuid, String bucketName,String parentUniqueId,String name) {
		Team team = team(uuid);
		FolderResource folderResource = team.addFolder(bucketName, parentUniqueId, SecurityContext.getEmail(), name);
		return conversionService.convert(folderResource, ResourceDTO.class);
	}

	@Override
	public ResourceDTO getContent(UUID uuid, String bucketName, String uniqueId) {

		Team team = team(uuid);
		ContentResource contentResource = team.getContent(SecurityContext.getEmail(),bucketName,uniqueId);
		return (ResourceDTO)conversionService.convert(contentResource,TypeDescriptor.valueOf(ContentResource.class), TypeDescriptor.valueOf(ResourceDTO.class));
	}


	public void sendNotification(String titolo, String messaggio, UUID uuid){


		//TODO: spostare in opportuno service con gestione eventi di notifica
		PushService pushService = new PushService();

		try {
			pushService.setPublicKey("BBYCxwATP2vVgw7mMPHJfT6bZrJP2iUV7OP_oxHzEcNFenrX66D8G34CdEmVULNg4WJXfjkeyT0AT9LwavpN8M4=");
			pushService.setPrivateKey("AKYLHgp-aV3kOys9Oy6QgxNI6OGIlOB3G6kjGvhl57j_");

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		WebPushMessage message = new WebPushMessage();
		message.title = titolo;
		message.message = messaggio;


		Team team = team(uuid);
		List<UserRole> members = team.getMembers();

		for (UserRole member: members) {
			System.out.println(">>>>>>>> " + member.getOid().getEmail() + " " + SecurityContext.getEmail());
			if (member.getOid().getEmail() != SecurityContext.getEmail()){
				Collection<WebPushSubscription> subscriptions = subscriptionsRegistry.getSubscriptions(member.getOid().getEmail());

				for (WebPushSubscription subscription: subscriptions) {
					System.out.println("Invio a  " + member.getOid().getEmail() + " Sono: " + SecurityContext.getEmail());
					Notification notification = null;
					if(true){
						try {
							notification = new Notification(
									subscription.getEndpoint(),
									subscription.getKeys().getP256dh(),
									subscription.getKeys().getAuth(),
									objectMapper.writeValueAsBytes(message));
						} catch (NoSuchAlgorithmException e) {
							e.printStackTrace();
						} catch (NoSuchProviderException e) {
							e.printStackTrace();
						} catch (InvalidKeySpecException e) {
							e.printStackTrace();
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}

						try {
							pushService.send(notification);
						} catch (GeneralSecurityException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (JoseException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}
			} else {
				System.out.println("************* non invio la notifica a te stesso...");
			}

		}

	}

	public void inviteNotification(String titolo, String messaggio, String destinatario){


		//TODO: spostare in opportuno service con gestione eventi di notifica
		PushService pushService = new PushService();

		try {
			pushService.setPublicKey("BBYCxwATP2vVgw7mMPHJfT6bZrJP2iUV7OP_oxHzEcNFenrX66D8G34CdEmVULNg4WJXfjkeyT0AT9LwavpN8M4=");
			pushService.setPrivateKey("AKYLHgp-aV3kOys9Oy6QgxNI6OGIlOB3G6kjGvhl57j_");

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		WebPushMessage message = new WebPushMessage();
		message.title = titolo;
		message.message = messaggio;

	Collection<WebPushSubscription> subscriptions = subscriptionsRegistry.getSubscriptions(destinatario);

				for (WebPushSubscription subscription: subscriptions) {

					Notification notification = null;
					if(true){
						try {
							notification = new Notification(
									subscription.getEndpoint(),
									subscription.getKeys().getP256dh(),
									subscription.getKeys().getAuth(),
									objectMapper.writeValueAsBytes(message));
						} catch (NoSuchAlgorithmException e) {
							e.printStackTrace();
						} catch (NoSuchProviderException e) {
							e.printStackTrace();
						} catch (InvalidKeySpecException e) {
							e.printStackTrace();
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}

						try {
							pushService.send(notification);
						} catch (GeneralSecurityException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (JoseException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}



	}

}
