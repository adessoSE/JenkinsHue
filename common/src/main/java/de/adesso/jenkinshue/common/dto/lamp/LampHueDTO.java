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
public class LampHueDTO implements Serializable {
	
	private static final long serialVersionUID = 7159578582668281049L;
	
	private String uniqueId;

	private String name;
	
	private String type;
	
	private String manufacturerName;
	
	private boolean lampOn;

}
