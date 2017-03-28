package de.adesso.jenkinshue.common.dto.lamp;

import java.util.List;

import de.adesso.jenkinshue.common.dto.scenario_config.ScenarioConfigDTO;

/**
 * 
 * @author wennier
 *
 */
public interface GroupedScenarioConfigsLamp {
	
	List<ScenarioConfigDTO> getBuildingConfigs();
	List<ScenarioConfigDTO> getFailureConfigs();
	List<ScenarioConfigDTO> getUnstableConfigs();
	List<ScenarioConfigDTO> getSuccessConfigs();
	
	void setBuildingConfigs(List<ScenarioConfigDTO> scenarioConfigs);
	void setFailureConfigs(List<ScenarioConfigDTO> scenarioConfigs);
	void setUnstableConfigs(List<ScenarioConfigDTO> scenarioConfigs);
	void setSuccessConfigs(List<ScenarioConfigDTO> scenarioConfigs);
	
}
