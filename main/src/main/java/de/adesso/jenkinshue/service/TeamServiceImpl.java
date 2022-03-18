package de.adesso.jenkinshue.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import de.adesso.jenkinshue.common.dto.team.TeamCreateDTO;
import de.adesso.jenkinshue.common.dto.team.TeamLampsDTO;
import de.adesso.jenkinshue.common.dto.team.TeamRenameDTO;
import de.adesso.jenkinshue.common.dto.team.TeamUpdateDTO;
import de.adesso.jenkinshue.common.dto.team.TeamUsersDTO;
import de.adesso.jenkinshue.common.enumeration.Scenario;
import de.adesso.jenkinshue.common.service.TeamService;
import de.adesso.jenkinshue.mapper.EntityMapper;
import de.adesso.jenkinshue.entity.Bridge;
import de.adesso.jenkinshue.entity.Team;
import de.adesso.jenkinshue.entity.User;
import de.adesso.jenkinshue.exception.EmptyInputException;
import de.adesso.jenkinshue.exception.TeamAlreadyExistsException;
import de.adesso.jenkinshue.exception.TeamDoesNotExistException;
import de.adesso.jenkinshue.repository.BridgeRepository;
import de.adesso.jenkinshue.repository.TeamRepository;
import de.adesso.jenkinshue.repository.UserRepository;
import de.adesso.jenkinshue.util.ScenarioUtil;

import static de.adesso.jenkinshue.util.ListUtil.nullSafe;

/**
 * @author wennier
 */
@Primary
@Service
public class TeamServiceImpl implements TeamService {

	private final TeamRepository teamRepository;

	private final UserRepository userRepository;

	private final BridgeRepository bridgeRepository;

	private final EntityMapper mapper;

	@Autowired
	public TeamServiceImpl(TeamRepository teamRepository, UserRepository userRepository, BridgeRepository bridgeRepository, EntityMapper mapper) {
		this.teamRepository = teamRepository;
		this.userRepository = userRepository;
		this.bridgeRepository = bridgeRepository;
		this.mapper = mapper;
	}

	private List<TeamUsersDTO> map(List<Team> teams) {
		List<TeamUsersDTO> dtos = new ArrayList<>();
		for (Team team : teams) {
			TeamUsersDTO dto = mapper.map(team, TeamUsersDTO.class);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public List<TeamUsersDTO> findAll() {
		return map(teamRepository.findAll());
	}

	@Override
	public List<TeamUsersDTO> findAll(int page, int size) {
		return map(teamRepository.findAll(PageRequest.of(page, size)).getContent());
	}

	@Override
	public long count() {
		return teamRepository.count();
	}

	@Override
	public List<TeamUsersDTO> findBySearchItem(String searchItem) {
		return map(teamRepository.findBySearchItem(searchItem.toLowerCase()));
	}

	@Override
	public List<TeamUsersDTO> findBySearchItem(String searchItem, int page, int size) {
		return map(teamRepository.findBySearchItem(searchItem.toLowerCase(), PageRequest.of(page, size)));
	}

	@Override
	public long count(String searchItem) {
		return teamRepository.count(searchItem.toLowerCase());
	}

	@Override
	public TeamLampsDTO create(TeamCreateDTO team) throws EmptyInputException, TeamAlreadyExistsException {
		if (team.getName() == null || team.getName().trim().isEmpty()) {
			throw new EmptyInputException();
		}
		team.setName(team.getName().trim());
		if (teamRepository.findByName(team.getName()) != null) {
			throw new TeamAlreadyExistsException(team.getName());
		}

		Team t = new Team();
		t.setName(team.getName());
		t.setScenarioPriority(new ScenarioUtil().generateDefaultScenarioPriority());
		t = teamRepository.save(t);

		t.getScenarioPriority().size();
		TeamLampsDTO dto = new TeamLampsDTO();
		dto.setId(t.getId());
		dto.setName(t.getName());
		dto.setScenarioPriority(t.getScenarioPriority());

		return dto;
	}

	@Override
	public TeamUsersDTO update(TeamUpdateDTO team) throws TeamDoesNotExistException {
		Team teamInDB = teamRepository.findById(team.getId()).orElseThrow(() -> new TeamDoesNotExistException(team.getId()));

		if (team.getScenarioPriority() != null && team.getScenarioPriority().size() == Scenario.getPriorityListLength()) {
			teamInDB.setScenarioPriority(team.getScenarioPriority());
			teamInDB = teamRepository.save(teamInDB);
			return mapper.map(teamInDB, TeamUsersDTO.class);
		} else {
			throw new IllegalArgumentException("Die priorisierte Liste enthält nicht alle Szenarios!");
		}

	}

	@Override
	public TeamUsersDTO rename(TeamRenameDTO team) throws TeamDoesNotExistException, TeamAlreadyExistsException, EmptyInputException {
		Team t = teamRepository.findById(team.getId()).orElseThrow(() -> new TeamDoesNotExistException(team.getId()));

		if (team.getName() == null || team.getName().trim().isEmpty()) { // Name ungueltig
			throw new EmptyInputException();
		} else if (t.getName().equals(team.getName())) { // Name hat sich nicht geaendert
			return mapper.map(t, TeamUsersDTO.class);
		}

		Team teamWithSameName = teamRepository.findByName(team.getName());
		if (teamWithSameName == null) {
			t.setName(team.getName());
			teamRepository.save(t);
			return mapper.map(t, TeamUsersDTO.class);
		} else {
			throw new TeamAlreadyExistsException(team.getName());
		}
	}


	// TODO warum funktioniert das hier ohne fetch?

	@Override
	public TeamUsersDTO findOne(long id) throws TeamDoesNotExistException {
		return  teamRepository.findById(id)
				.map(team -> mapper.map(team, TeamUsersDTO.class))
				.orElseThrow(() -> new TeamDoesNotExistException(id));
	}

	@Override
	public void remove(long id) {
		List<User> users = userRepository.findAllOfTeam(id);
		for (User user : users) {
			List<Bridge> bridges = user.getBridges();
			for (Bridge bridge : nullSafe(bridges)) {
				bridge.setUser(null);
				bridgeRepository.save(bridge);
			}
			userRepository.deleteById(user.getId());
		}
		teamRepository.deleteById(id);
	}

}
