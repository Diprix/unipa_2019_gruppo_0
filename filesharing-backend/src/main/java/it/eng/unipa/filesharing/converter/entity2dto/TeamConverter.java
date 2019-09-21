package it.eng.unipa.filesharing.converter.entity2dto;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import it.eng.unipa.filesharing.dto.TeamDTO;
import it.eng.unipa.filesharing.model.Team;

@Component
public class TeamConverter implements Converter<Team, TeamDTO>{
	
	@Autowired
	private UserRoleConverter userRoleConverter;
	
	@Autowired
	private BucketConverter bucketConverter;



	@Override
	public TeamDTO convert(Team team) {
		TeamDTO teamDTO = null;
		if(team !=null) {
			teamDTO = new TeamDTO();
			teamDTO.setDescription(team.getDescription());
			teamDTO.setName(team.getName());
			teamDTO.setUuid(team.getUuid());
			teamDTO.setMembers(team.getMembers().stream().map((t)->userRoleConverter.convert(t)).collect(Collectors.toList()));
			teamDTO.setBuckets(team.getBuckets().stream().map((t)->bucketConverter.convert(t)).collect(Collectors.toList()));
		}
		return teamDTO;
	}

}
