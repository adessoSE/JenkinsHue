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
public class LampNameDTO implements LampWithHueUniqueId, Serializable {

	private static final long serialVersionUID = 2167489531871740128L;

	private long id;
	
	private String hueUniqueId;

	private String name;
	
}
