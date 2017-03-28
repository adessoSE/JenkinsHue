package de.adesso.jenkinshue.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

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
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BridgeRepository bridgeRepository;
	
	@Autowired
	private LDAPManager ldapManager;

	@Autowired
	private Mapper mapper;
	
	private List<UserDTO> map(List<User> users) {
		List<UserDTO> dtos = new ArrayList<>();
		for(User user : users) {
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
	public UserDTO create(UserCreateDTO user) throws UserAlreadyExistsException, InvalidLoginException {
		User ldapUser = ldapManager.getUserForLoginName(user.getLogin().toLowerCase());
		
		if(ldapUser != null) {
			if(userRepository.findByEmail(ldapUser.getEmail().toLowerCase()) != null) {
				throw new UserAlreadyExistsException(ldapUser.getEmail().toLowerCase());
			}
			
			ldapUser.setEmail(ldapUser.getEmail().toLowerCase());
			ldapUser.setTeam(teamRepository.findOne(user.getTeamId()));
			
			ldapUser = userRepository.save(ldapUser);
			
			return mapper.map(ldapUser, UserDTO.class);
		} else {
			throw new InvalidLoginException(user.getLogin());
		}
	}
	
	@Override
	public UserDTO update(UserUpdateDTO user) {
		User userInDB = userRepository.findOne(user.getId());
		userInDB.setRoles(user.getRoles());
		userInDB = userRepository.save(userInDB);
		return mapper.map(userInDB, UserDTO.class);
	}

	@Override
	public void remove(long id) {
		User user = userRepository.findOne(id);
		if(user != null) {
			List<Bridge> bridges = user.getBridges();
			for(Bridge bridge : bridges) {
				bridge.setUser(null);
				bridgeRepository.save(bridge);
			}
			userRepository.delete(id);
		}
	}

	@Override
	public UserDTO findByLogin(String login) {
		User user = userRepository.findByLogin(login);
		if(user != null) {
			return mapper.map(user, UserDTO.class);
		} else {
			return null;
		}
	}

}
