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
public class LightDTO implements Serializable {
	
	private static final long serialVersionUID = -7143861028203898422L;
	
	private String uniqueid;

}
