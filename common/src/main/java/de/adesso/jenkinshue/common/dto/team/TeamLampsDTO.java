package de.adesso.jenkinshue.common.dto.team;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import de.adesso.jenkinshue.common.dto.job.JobDTO;
import de.adesso.jenkinshue.common.dto.lamp.GroupedScenarioConfigsLamp;
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
public class TeamLampsDTO implements Serializable {
	
	private static final long serialVersionUID = 2176492368428505851L;

	private long id;
	
	private String name;
	
	private List<Scenario> scenarioPriority;
	
	private List<LampDTO> lamps;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class LampDTO implements Serializable, GroupedScenarioConfigsLamp {
		
		private static final long serialVersionUID = -7567638219788953738L;

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
		
	}

}