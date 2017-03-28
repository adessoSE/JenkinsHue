package de.adesso.jenkinshue.common.service;

import java.util.List;

import de.adesso.jenkinshue.common.dto.lamp.LampCreateDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampGroupedScenariosDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampHueDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampNameDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampRenameDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampTestDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampTurnOffDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampUpdateDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampUpdateLastShownScenarioDTO;
import de.adesso.jenkinshue.common.dto.team.TeamLampsDTO;

/**
 * 
 * @author wennier
 *
 */
public interface LampService {
	
	LampDTO create(LampCreateDTO lamp);
	
	LampDTO update(LampUpdateDTO lamp);
	
	LampDTO updateLastShownScenario(LampUpdateLastShownScenarioDTO lamp);
	
	LampDTO rename(LampRenameDTO lamp);
	
	void remove(long id);
	
	List<LampDTO> findAll();
	
	LampGroupedScenariosDTO findOne(long id);
	
	long count();
	
	long count(long teamId);
	
	List<LampHueDTO> findAllAvailable();
	
	TeamLampsDTO findAllOfATeam(long teamId);
	
	List<LampNameDTO> findAllLampNamesOfATeam(long teamId);
	
	void testScenario(LampTestDTO lamp);
	
	void pulseOnce(String hueUniqueId);
	
	void turnOff(LampTurnOffDTO lamp);

}
