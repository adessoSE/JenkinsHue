package de.adesso.jenkinshue.common.dto.lamp;

import java.io.Serializable;
import java.util.List;

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
public class LampTurnOffDTO implements Serializable {
	
	private static final long serialVersionUID = 658817298774615873L;

	private List<LampNameDTO> lamps;

}
