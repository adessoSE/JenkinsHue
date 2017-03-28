package de.adesso.jenkinshue.common.service;

import java.util.List;

import de.adesso.jenkinshue.common.dto.user.UserCreateDTO;
import de.adesso.jenkinshue.common.dto.user.UserDTO;
import de.adesso.jenkinshue.common.dto.user.UserUpdateDTO;

/**
 * 
 * @author wennier
 *
 */
public interface UserService {
	
	List<UserDTO> findAll();
	
	List<UserDTO> findAll(int page, int size);
	
	long count();
	
	List<UserDTO> findBySearchItem(String searchItem);
	
	List<UserDTO> findBySearchItem(String searchItem, int page, int size);
	
	long count(String searchItem);
	
	long count(long teamId);
	
	UserDTO create(UserCreateDTO user);
	
	UserDTO update(UserUpdateDTO user);
	
	void remove(long id);
	
	UserDTO findByLogin(String login);
	
}
