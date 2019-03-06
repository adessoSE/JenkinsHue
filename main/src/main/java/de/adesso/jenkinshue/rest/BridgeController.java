package de.adesso.jenkinshue.rest;

import java.util.List;

import javax.validation.Valid;

import de.adesso.jenkinshue.exception.UserDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.adesso.jenkinshue.common.dto.bridge.BridgeCreateDTO;
import de.adesso.jenkinshue.common.dto.bridge.BridgeDTO;
import de.adesso.jenkinshue.common.hue.dto.FoundBridgeDTO;
import de.adesso.jenkinshue.common.service.BridgeService;
import de.adesso.jenkinshue.exception.BridgeAlreadyExistsException;
import de.adesso.jenkinshue.exception.InvalidIpException;

/**
 *
 * @author wennier
 *
 */
@RestController
@RequestMapping("/rest/bridges")
public class BridgeController implements BridgeService {

	private final BridgeService bridgeService;

	@Autowired
	public BridgeController(BridgeService bridgeService) {
		this.bridgeService = bridgeService;
	}

	@Override
	public List<BridgeDTO> findAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<BridgeDTO> findAll(int page, int size) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	@RequestMapping("/available")
	public List<FoundBridgeDTO> findAvailable() {
		return bridgeService.findAvailable();
	}

	@Override
	public long count() {
		throw new UnsupportedOperationException();
	}

	@Override
	@RequestMapping
	public List<BridgeDTO> findBySearchItem(@RequestParam(required = false) String searchItem) {
		if(searchItem == null || searchItem.isEmpty()) {
			return bridgeService.findAll();
		} else {
			return bridgeService.findBySearchItem(searchItem);
		}
	}

	@Override
	@RequestMapping("/{page}/{size}")
	public List<BridgeDTO> findBySearchItem(@RequestParam(required = false) String searchItem, @PathVariable int page, @PathVariable int size) {
		if(searchItem == null || searchItem.isEmpty()) {
			return bridgeService.findAll(page, size);
		} else {
			return bridgeService.findBySearchItem(searchItem, page, size);
		}
	}

	@Override
	@RequestMapping("/count")
	public long count(@RequestParam(required = false) String searchItem) {
		if(searchItem == null || searchItem.isEmpty()) {
			return bridgeService.count();
		} else {
			return bridgeService.count(searchItem);
		}
	}

	@Override
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public BridgeDTO create(@RequestBody @Valid BridgeCreateDTO bridge) throws InvalidIpException,
			BridgeAlreadyExistsException, UserDoesNotExistException {
		return bridgeService.create(bridge);
	}

	@Override
	public BridgeDTO findByIp(String ip) {
		throw new UnsupportedOperationException();
	}

	@Override
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.DELETE)
	public void remove(@PathVariable long id) {
		bridgeService.remove(id);
	}

}
