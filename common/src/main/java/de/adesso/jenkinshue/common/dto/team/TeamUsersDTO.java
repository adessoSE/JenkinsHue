package de.adesso.jenkinshue.common.dto.team;

import java.io.Serializable;
import java.util.List;

import de.adesso.jenkinshue.common.enumeration.Role;
import de.adesso.jenkinshue.common.enumeration.Scenario;
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
public class TeamUsersDTO implements Serializable {
	
	private static final long serialVersionUID = -2751080802909851205L;

	private long id;
	
	private String name;
	
	private List<Scenario> scenarioPriority;
	
	private List<UserDTO> users;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UserDTO implements Serializable {
		
		private static final long serialVersionUID = 713609152508817433L;
		
		private long id;
		
		private String email;
		
		private String login;
		
		private String forename;
		
		private String surname;
		
		private List<Role> roles;
		
		/*private List<BridgeDTO> bridges;
		
		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class BridgeDTO implements Serializable {
			
			private static final long serialVersionUID = -2559524612111310272L;

			private long id;
			
			private String ip;
			
			private String hueUserName;
			
		}*/
		
	}
	
}
