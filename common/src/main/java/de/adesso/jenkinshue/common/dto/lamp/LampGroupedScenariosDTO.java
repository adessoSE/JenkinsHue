package de.adesso.jenkinshue.common.dto.lamp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import de.adesso.jenkinshue.common.dto.job.JobDTO;
import de.adesso.jenkinshue.common.dto.scenario_config.ScenarioConfigDTO;
import de.adesso.jenkinshue.common.enumeration.Scenario;
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
public class LampGroupedScenariosDTO implements Serializable, LampWithHueUniqueId, GroupedScenarioConfigsLamp {
	
	private static final long serialVersionUID = -5057061266421515099L;

	private long id;

	private String hueUniqueId;

	private String name;

	private Date workingStart;

	private Date workingEnd;

	private List<JobDTO> jobs;
	
	private Scenario lastShownScenario;

	private List<ScenarioConfigDTO> buildingConfigs;
	
	private List<ScenarioConfigDTO> failureConfigs;
	
	private List<ScenarioConfigDTO> unstableConfigs;
	
	private List<ScenarioConfigDTO> successConfigs;
	
	private LampGroupedScenariosDTO_TeamDTO team;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class LampGroupedScenariosDTO_TeamDTO implements Serializable {
		
		private static final long serialVersionUID = -6453776040086323825L;

		private long id;
		
		private String name;
		
		private List<Scenario> scenarioPriority;
		
	}
	
}
