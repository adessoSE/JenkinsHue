package de.adesso.jenkinshue.common.dto.bridge;

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
public class BridgeDTO implements Serializable {
	
	private static final long serialVersionUID = 57424615952440456L;
	
	private long id;
	
	private String ip;
	
	private String hueUserName;
	
	private UserDTO user;
	
	private String state;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UserDTO implements Serializable {
		
		private static final long serialVersionUID = 6103691782171217524L;
		
		private long id;

		private String email;
		
		private String forename;
		
		private String surname;
		
		private List<Role> roles;
		
	}

}
