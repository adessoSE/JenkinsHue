package de.adesso.jenkinshue.common.dto.job;

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
public class JobDTO implements Serializable {
	
	private static final long serialVersionUID = 4541954011434830296L;

	private long id;
	
	private String name;
	
}
