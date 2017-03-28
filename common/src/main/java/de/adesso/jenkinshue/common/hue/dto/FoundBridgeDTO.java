package de.adesso.jenkinshue.common.hue.dto;

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
public class FoundBridgeDTO implements Serializable {
	
	private static final long serialVersionUID = 6311255645585348785L;
	
	private String id;
	
	private String internalipaddress;
	
	private String macaddress;
	
	private String name;

}
