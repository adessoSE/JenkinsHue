package de.adesso.jenkinshue.common.dto.scenario_config;

import java.io.Serializable;

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
public class ScenarioConfigDTO implements Serializable {
	
	private static final long serialVersionUID = 5192317471221328814L;
	
	private long id;
	
	private Scenario scenario;
	
	private boolean lampOn;
	
	private boolean onetimePulsationEnabled;
	
	private boolean onetimePulsationColorChangeEnabled;
	
	private String onetimePulsationColorHex;
	
	private boolean colorChangeEnabled;
	
	private String colorHex;
	
	private boolean brightnessChangeEnabled;
	
	private int brightness;

}
