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
public class TeamRenameDTO implements Serializable {

	private static final long serialVersionUID = -7697304462700218471L;

	private long id;
	
	private String name;
	
}
