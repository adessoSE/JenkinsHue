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

import de.adesso.jenkinshue.common.dto.user.UserCreateDTO;
import de.adesso.jenkinshue.common.dto.user.UserDTO;
import de.adesso.jenkinshue.common.dto.user.UserUpdateDTO;
import de.adesso.jenkinshue.common.service.UserService;
import de.adesso.jenkinshue.exception.InvalidLoginException;
import de.adesso.jenkinshue.exception.UserAlreadyExistsException;

/**
 *
 * @author wennier
 *
 */
@RestController
@RequestMapping("/rest/users")
public class UserController implements UserService {
	
	@Autowired
	private UserService userService;

	@Override
	public List<UserDTO> findAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<UserDTO> findAll(int page, int size) {
		throw new UnsupportedOperationException();
	}

	@Override
	public long count() {
		throw new UnsupportedOperationException();
	}

	@Override
	@RequestMapping
	public List<UserDTO> findBySearchItem(@RequestParam(required = false) String searchItem) {
		if(searchItem == null || searchItem.isEmpty()) {
			return userService.findAll();
		} else {
			return userService.findBySearchItem(searchItem);
		}
	}

	@Override
	@RequestMapping(value = "/{page}/{size}")
	public List<UserDTO> findBySearchItem(@RequestParam(required = false) String searchItem, @PathVariable int page, @PathVariable int size) {
		if(searchItem == null || searchItem.isEmpty()) {
			return userService.findAll(page, size);
		} else {
			return userService.findBySearchItem(searchItem, page, size);
		}
	}

	@Override
	@RequestMapping("/count")
	public long count(@RequestParam(required = false) String searchItem) {
		if(searchItem == null || searchItem.isEmpty()) {
			return userService.count();
		} else {
			return userService.count(searchItem);
		}
	}
	
	@Override
	public long count(long teamId) {
		throw new UnsupportedOperationException();
	}

	@Override
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public UserDTO create(@RequestBody @Valid UserCreateDTO user) throws UserAlreadyExistsException, InvalidLoginException {
		return userService.create(user);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public UserDTO update(@RequestBody @Valid UserUpdateDTO user) {
		return userService.update(user);
	}

	@Override
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.DELETE)
	public void remove(@PathVariable long id) {
		userService.remove(id);
	}

	@Override
	public UserDTO findByLogin(String login) {
		throw new UnsupportedOperationException();
	}

}
