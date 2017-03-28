package de.adesso.jenkinshue.common.dto.team;

import java.io.Serializable;

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
public class TeamCreateDTO implements Serializable {
	
	private static final long serialVersionUID = 5201779605583701696L;
	
	private String name;

}
