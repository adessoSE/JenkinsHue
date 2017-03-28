package de.adesso.jenkinshue.common.dto.lamp;

import java.io.Serializable;
import java.util.List;

import de.adesso.jenkinshue.common.dto.scenario_config.ScenarioConfigDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author wennier
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LampTestDTO implements Serializable {
	
	private static final long serialVersionUID = 4331715489200965121L;
	
	private List<LampNameDTO> lamps;
	
	private ScenarioConfigDTO scenarioConfig;

}
