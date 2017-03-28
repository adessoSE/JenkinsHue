package de.adesso.jenkinshue.common.dto.team;

import java.io.Serializable;
import java.util.List;

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
public class TeamUpdateDTO implements Serializable {
	
	private static final long serialVersionUID = 4641986498788373321L;
	
	private long id;
	
	private List<Scenario> scenarioPriority;

}
