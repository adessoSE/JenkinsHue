package de.adesso.jenkinshue.common.dto.lamp;

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
public class LampUpdateLastShownScenarioDTO implements Serializable {
	
	private static final long serialVersionUID = 2543092159046593681L;

	private long id;

	private Scenario lastShownScenario;
	
}
