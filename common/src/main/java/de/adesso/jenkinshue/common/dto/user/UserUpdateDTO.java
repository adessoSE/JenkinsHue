package de.adesso.jenkinshue.common.dto.user;

import java.io.Serializable;
import java.util.List;

import de.adesso.jenkinshue.common.enumeration.Role;
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
public class UserUpdateDTO implements Serializable {
	
	private static final long serialVersionUID = -8934160958007134201L;

	private long id;
	
	private List<Role> roles;

}
