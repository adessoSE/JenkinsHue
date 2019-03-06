package de.adesso.jenkinshue.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.adesso.jenkinshue.common.dto.team.TeamCreateDTO;
import de.adesso.jenkinshue.common.dto.team.TeamLampsDTO;
import de.adesso.jenkinshue.common.dto.team.TeamRenameDTO;
import de.adesso.jenkinshue.common.dto.team.TeamUpdateDTO;
import de.adesso.jenkinshue.common.dto.team.TeamUsersDTO;
import de.adesso.jenkinshue.common.service.TeamService;
import de.adesso.jenkinshue.exception.EmptyInputException;
import de.adesso.jenkinshue.exception.TeamAlreadyExistsException;
import de.adesso.jenkinshue.exception.TeamDoesNotExistException;

/**
 * 
 * @author wennier
 *
 */
@RestController
@RequestMapping("/rest/teams")
public class TeamController implements TeamService {

	private final TeamService teamService;

	@Autowired
	public TeamController(TeamService teamService) {
		this.teamService = teamService;
	}

	@Override
	public List<TeamUsersDTO> findAll() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public List<TeamUsersDTO> findAll(int page, int size) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public long count() {
		throw new UnsupportedOperationException();
	}

	@Override
	@RequestMapping
	public List<TeamUsersDTO> findBySearchItem(@RequestParam(required = false) String searchItem) {
		if(searchItem == null || searchItem.isEmpty()) {
			return teamService.findAll();
		} else {
			return teamService.findBySearchItem(searchItem);
		}
	}

	@Override
	@RequestMapping(value = "/{page}/{size}")
	public List<TeamUsersDTO> findBySearchItem(@RequestParam(required = false) String searchItem, @PathVariable int page, @PathVariable int size) {
		if(searchItem == null || searchItem.isEmpty()) {
			return teamService.findAll(page, size);
		} else {
			return teamService.findBySearchItem(searchItem, page, size);
		}
	}
	
	@Override
	@RequestMapping("/count")
	public long count(@RequestParam(required = false) String searchItem) {
		if(searchItem == null || searchItem.isEmpty()) {
			return teamService.count();
		} else {
			return teamService.count(searchItem);
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public TeamLampsDTO create(@RequestBody @Valid TeamCreateDTO team) throws EmptyInputException, TeamAlreadyExistsException {
		return teamService.create(team);
	}

	@Override
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public TeamUsersDTO update(@RequestBody @Valid TeamUpdateDTO team) throws TeamDoesNotExistException {
		return teamService.update(team);
	}

	@Override
	@RequestMapping(value = "/rename", method = RequestMethod.POST)
	public TeamUsersDTO rename(@RequestBody @Valid TeamRenameDTO team)
			throws TeamDoesNotExistException, TeamAlreadyExistsException, EmptyInputException {
		return teamService.rename(team);
	}

	@Override
	@RequestMapping("/{id}")
	public TeamUsersDTO findOne(@PathVariable long id) throws TeamDoesNotExistException {
		return teamService.findOne(id);
	}
	
	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.DELETE)
	public void remove(@PathVariable long id) {
		teamService.remove(id);
	}

}
