package de.adesso.jenkinshue.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import de.adesso.jenkinshue.entity.Team;
import de.adesso.jenkinshue.exception.EmptyInputException;
import de.adesso.jenkinshue.exception.TeamDoesNotExistException;
import de.adesso.jenkinshue.exception.UserDoesNotExistException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import de.adesso.jenkinshue.common.dto.user.UserCreateDTO;
import de.adesso.jenkinshue.common.dto.user.UserDTO;
import de.adesso.jenkinshue.common.dto.user.UserUpdateDTO;
import de.adesso.jenkinshue.common.service.UserService;
import de.adesso.jenkinshue.dozer.Mapper;
import de.adesso.jenkinshue.entity.Bridge;
import de.adesso.jenkinshue.entity.User;
import de.adesso.jenkinshue.exception.InvalidLoginException;
import de.adesso.jenkinshue.exception.UserAlreadyExistsException;
import de.adesso.jenkinshue.repository.BridgeRepository;
import de.adesso.jenkinshue.repository.TeamRepository;
import de.adesso.jenkinshue.repository.UserRepository;
import de.adesso.jenkinshue.util.LDAPManager;

/**
 *
 * @author wennier
 *
 */
@Primary
@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final TeamRepository teamRepository;

	private final UserRepository userRepository;

	private final BridgeRepository bridgeRepository;

	@Autowired(required = false)
	private LDAPManager ldapManager;

	private final Mapper mapper;

	@Autowired
	public UserServiceImpl(TeamRepository teamRepository, UserRepository userRepository, BridgeRepository bridgeRepository, Mapper mapper) {
		this.teamRepository = teamRepository;
		this.userRepository = userRepository;
		this.bridgeRepository = bridgeRepository;
		this.mapper = mapper;
	}

	private List<UserDTO> map(List<User> users) {
		List<UserDTO> dtos = new ArrayList<>();
		for (User user : users) {
			UserDTO dto = mapper.map(user, UserDTO.class);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public List<UserDTO> findAll() {
		return map(userRepository.findAll());
	}

	@Override
	public List<UserDTO> findAll(int page, int size) {
		return map(userRepository.findAll(new PageRequest(page, size)).getContent());
	}

	@Override
	public long count() {
		return userRepository.count();
	}

	@Override
	public List<UserDTO> findBySearchItem(String searchItem) {
		return map(userRepository.findBySearchItem(searchItem.toLowerCase()));
	}

	@Override
	public List<UserDTO> findBySearchItem(String searchItem, int page, int size) {
		return map(userRepository.findBySearchItem(searchItem.toLowerCase(), new PageRequest(page, size)));
	}

	@Override
	public long count(String searchItem) {
		return userRepository.count(searchItem.toLowerCase());
	}

	@Override
	public long count(long teamId) {
		return userRepository.count(teamId);
	}

	@Override
	public UserDTO create(UserCreateDTO user) throws EmptyInputException, TeamDoesNotExistException,
			UserAlreadyExistsException, InvalidLoginException {
		if (user.getLogin() == null || user.getLogin().trim().isEmpty()) {
			throw new EmptyInputException();
		}
		user.setLogin(user.getLogin().trim().toLowerCase());
		Team team = teamRepository.findOne(user.getTeamId());
		if (team == null) {
			throw new TeamDoesNotExistException(user.getTeamId());
		}
		if (userRepository.findByLogin(user.getLogin()) != null) {
			throw new UserAlreadyExistsException(user.getLogin());
		}

		User u;
		//noinspection ConstantConditions
		if (ldapManager != null) {
			u = ldapManager.getUserForLoginName(user.getLogin());
			if (u == null) {
				throw new InvalidLoginException(user.getLogin());
			}
		} else {
			u = new User();
			u.setLogin(user.getLogin());
		}

		u.setTeam(team);
		u = userRepository.save(u);

		return mapper.map(u, UserDTO.class);
	}

	@Override
	public UserDTO update(UserUpdateDTO user) throws UserDoesNotExistException {
		User userInDB = userRepository.findOne(user.getId());
		if (userInDB == null) {
			throw new UserDoesNotExistException(user.getId());
		}
		userInDB.setRoles(user.getRoles());
		userInDB = userRepository.save(userInDB);
		return mapper.map(userInDB, UserDTO.class);
	}

	@Override
	public void remove(long id) {
		User user = userRepository.findOne(id);
		if (user != null) {
			List<Bridge> bridges = user.getBridges();
			for (Bridge bridge : bridges) {
				bridge.setUser(null);
				bridgeRepository.save(bridge);
			}
			userRepository.delete(id);
		}
	}

	@Override
	public UserDTO findByLogin(String login) {
		User user = userRepository.findByLogin(login);
		if (user != null) {
			return mapper.map(user, UserDTO.class);
		} else {
			return null;
		}
	}

}
