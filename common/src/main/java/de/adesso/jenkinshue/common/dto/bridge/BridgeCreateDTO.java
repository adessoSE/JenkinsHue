package de.adesso.jenkinshue.common.dto.bridge;

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
public class BridgeCreateDTO implements Serializable {
	
	private static final long serialVersionUID = 8839325147208013702L;
	
	private String ip;
	
	private long userId;

}