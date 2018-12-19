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
public class UserDTO implements Serializable {
	
	private static final long serialVersionUID = 713609152508817433L;
	
	private long id;
	
	private String email;
	
	private String login;
	
	private String forename;
	
	private String surname;
	
	private UserDTO_TeamDTO team;
	
	private List<UserDTO_BridgeDTO> bridges;
	
	private List<Role> roles;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UserDTO_TeamDTO implements Serializable {
		
		private static final long serialVersionUID = -6733627068464578062L;

		private long id;
		
		private String name;

	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UserDTO_BridgeDTO implements Serializable {
		
		private static final long serialVersionUID = -2559524612111310272L;

		private long id;
		
		private String ip;
		
		private String hueUserName;
		
	}
	
}
