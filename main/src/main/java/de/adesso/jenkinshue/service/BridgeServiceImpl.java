package de.adesso.jenkinshue.service;

import java.util.ArrayList;
import java.util.List;

import de.adesso.jenkinshue.entity.User;
import de.adesso.jenkinshue.exception.UserDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import de.adesso.jenkinshue.common.dto.bridge.BridgeCreateDTO;
import de.adesso.jenkinshue.common.dto.bridge.BridgeDTO;
import de.adesso.jenkinshue.common.hue.dto.FoundBridgeDTO;
import de.adesso.jenkinshue.common.service.BridgeService;
import de.adesso.jenkinshue.common.service.HueService;
import de.adesso.jenkinshue.mapper.EntityMapper;
import de.adesso.jenkinshue.entity.Bridge;
import de.adesso.jenkinshue.exception.BridgeAlreadyExistsException;
import de.adesso.jenkinshue.exception.InvalidIpException;
import de.adesso.jenkinshue.repository.BridgeRepository;
import de.adesso.jenkinshue.repository.UserRepository;

/**
 * @author wennier
 */
@Primary
@Service
public class BridgeServiceImpl implements BridgeService {

	private final UserRepository userRepository;

	private final BridgeRepository bridgeRepository;

	private final HueService hueService;

	private final EntityMapper mapper;

	@Autowired
	public BridgeServiceImpl(UserRepository userRepository, BridgeRepository bridgeRepository, HueService hueService, EntityMapper mapper) {
		this.userRepository = userRepository;
		this.bridgeRepository = bridgeRepository;
		this.hueService = hueService;
		this.mapper = mapper;
	}

	private List<BridgeDTO> map(List<Bridge> bridges) {
		List<BridgeDTO> dtos = new ArrayList<>();
		for (Bridge bridge : bridges) {
			BridgeDTO dto = mapper.map(bridge, BridgeDTO.class);
			dtos.add(dto);
		}
		hueService.updateBridgeState(dtos);
		return dtos;
	}

	@Override
	public List<BridgeDTO> findAll() {
		return map(bridgeRepository.findAll());
	}

	@Override
	public List<BridgeDTO> findAll(int page, int size) {
		return map(bridgeRepository.findAll(PageRequest.of(page, size)).getContent());
	}

	@Override
	public List<FoundBridgeDTO> findAvailable() {
		List<FoundBridgeDTO> allBridges = hueService.findAllBridges();
		for (int i = allBridges.size() - 1; i >= 0; i--) {
			String ip = allBridges.get(i).getInternalipaddress();
			if (bridgeRepository.findByIp(ip) != null) {
				allBridges.remove(i);
			}
		}
		return allBridges;
	}

	@Override
	public long count() {
		return bridgeRepository.count();
	}

	@Override
	public List<BridgeDTO> findBySearchItem(String searchItem) {
		return map(bridgeRepository.findBySearchItem(searchItem.toLowerCase()));
	}

	@Override
	public List<BridgeDTO> findBySearchItem(String searchItem, int page, int size) {
		return map(bridgeRepository.findBySearchItem(searchItem.toLowerCase(), PageRequest.of(page, size)));
	}

	@Override
	public long count(String searchItem) {
		return bridgeRepository.count(searchItem.toLowerCase());
	}

	@Override
	public BridgeDTO create(BridgeCreateDTO bridge) throws InvalidIpException, BridgeAlreadyExistsException,
			UserDoesNotExistException {
		if (!isIPv4Address(bridge.getIp())) {
			throw new InvalidIpException(bridge.getIp());
		} else if (bridgeRepository.findByIp(bridge.getIp()) != null) {
			throw new BridgeAlreadyExistsException(bridge.getIp());
		}
		User user = userRepository.findById(bridge.getUserId())
				.orElseThrow(() -> new UserDoesNotExistException(bridge.getUserId()));

		Bridge b = new Bridge();
		b.setIp(bridge.getIp());
		b.setUser(user);

		b = bridgeRepository.save(b);

		BridgeDTO dto = mapper.map(b, BridgeDTO.class);

		hueService.connectToBridgeIfNotAlreadyConnected(dto);
//		hueService.updateBridgeState(Arrays.asList(dto)); nicht noetig, da die Bridge nach einer Verzögerung erneut abgefragt werden

		return dto;
	}

	@Override
	public BridgeDTO findByIp(String ip) {
		return mapper.map(bridgeRepository.findByIp(ip), BridgeDTO.class);
	}

	@Override
	public void remove(long id) {
		bridgeRepository.findById(id).ifPresent(bridge -> {

			hueService.disconnectFromBridge(mapper.map(bridge, BridgeDTO.class));
			bridgeRepository.deleteById(id);
		});

	}

	boolean isIPv4Address(String ip) {
		if (ip == null) {
			return false;
		} else {
			String regex255 = "(([0-9])|([1-9][0-9])|(1[0-9][0-9])|(2[0-4][0-9])|(25[0-5]))";
			String regexIPv4 = regex255 + "\\." + regex255 + "\\." + regex255 + "\\." + regex255;
			return ip.matches(regexIPv4);
		}
	}

}
