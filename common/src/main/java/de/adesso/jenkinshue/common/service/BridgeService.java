package de.adesso.jenkinshue.common.service;

import java.util.List;

import de.adesso.jenkinshue.common.dto.bridge.BridgeCreateDTO;
import de.adesso.jenkinshue.common.dto.bridge.BridgeDTO;
import de.adesso.jenkinshue.common.hue.dto.FoundBridgeDTO;

/**
 * 
 * @author wennier
 *
 */
public interface BridgeService {
	
	List<BridgeDTO> findAll();
	
	List<BridgeDTO> findAll(int page, int size);
	
	List<FoundBridgeDTO> findAvailable();
	
	long count();
	
	List<BridgeDTO> findBySearchItem(String searchItem);
	
	List<BridgeDTO> findBySearchItem(String searchItem, int page, int size);
	
	long count(String searchItem);

	BridgeDTO create(BridgeCreateDTO bridge);
	
	BridgeDTO findByIp(String ip);
	
	void remove(long id);
	
}
