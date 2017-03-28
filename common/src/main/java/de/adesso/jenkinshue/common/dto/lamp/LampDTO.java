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
public class LampDTO implements LampWithHueUniqueId, Serializable {
	
	private static final long serialVersionUID = -8488674575373871293L;

	private long id;

	private String hueUniqueId;

	private String name;

	private Date workingStart;

	private Date workingEnd;

	private List<JobDTO> jobs;
	
	private Scenario lastShownScenario;

	private List<ScenarioConfigDTO> scenarioConfigs;
	
	private TeamDTO team;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TeamDTO implements Serializable {
		
		private static final long serialVersionUID = 2784384297925830509L;

		private long id;
		
		private String name;
		
		private List<Scenario> scenarioPriority;
		
	}
	
}
