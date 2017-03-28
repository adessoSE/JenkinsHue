package de.adesso.jenkinshue.common.service;

import java.util.List;

import de.adesso.jenkinshue.common.dto.bridge.BridgeDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampHueDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampTestDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampTurnOffDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampWithHueUniqueId;
import de.adesso.jenkinshue.common.dto.scenario_config.ScenarioConfigDTO;
import de.adesso.jenkinshue.common.hue.dto.FoundBridgeDTO;

public interface HueService {
	
	void connectToBridgeIfNotAlreadyConnected(BridgeDTO bridge);
	
	void disconnectFromBridge(BridgeDTO bridge);
	
	void showRandomColorsOnAllLamps();
	
	void updateLamp(LampWithHueUniqueId lamp, ScenarioConfigDTO config);
	
	List<LampHueDTO> findAllLamps();
	
	List<FoundBridgeDTO> findAllBridges();
	
	void testScenario(LampTestDTO lamp);
	
	void pulseOnce(String hueUniqueId);

	void turnOff(LampTurnOffDTO lamp);
	
	void updateBridgeState(List<BridgeDTO> bridgeDTOs);
	
	boolean ipAreEqual(String ip1, String ip2);
	
}
