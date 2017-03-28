package de.adesso.jenkinshue.common.dto.lamp;

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
public class LampCreateDTO implements Serializable {
	
	private static final long serialVersionUID = 7386734738061212997L;
	
	private String hueUniqueId;

	private String name;

	private long teamId;

}
