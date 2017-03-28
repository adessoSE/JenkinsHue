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
public class LampRenameDTO implements Serializable {

	private static final long serialVersionUID = 1362885206455850540L;
	
	private long id;

	private String name;

}
