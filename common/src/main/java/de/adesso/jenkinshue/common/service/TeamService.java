package de.adesso.jenkinshue.common.service;

import java.util.List;

import de.adesso.jenkinshue.common.dto.team.TeamCreateDTO;
import de.adesso.jenkinshue.common.dto.team.TeamLampsDTO;
import de.adesso.jenkinshue.common.dto.team.TeamRenameDTO;
import de.adesso.jenkinshue.common.dto.team.TeamUpdateDTO;
import de.adesso.jenkinshue.common.dto.team.TeamUsersDTO;

/**
 * 
 * @author wennier
 *
 */
public interface TeamService {
	
	List<TeamUsersDTO> findAll();
	
	List<TeamUsersDTO> findAll(int page, int size);
	
	long count();
	
	List<TeamUsersDTO> findBySearchItem(String searchItem);
	
	List<TeamUsersDTO> findBySearchItem(String searchItem, int page, int size);
	
	long count(String searchItem);
	
	TeamLampsDTO create(TeamCreateDTO team);
	
	TeamUsersDTO update(TeamUpdateDTO team);
	
	TeamUsersDTO rename(TeamRenameDTO team);
	
	TeamUsersDTO findOne(long id);
	
	void remove(long id);
	
}
