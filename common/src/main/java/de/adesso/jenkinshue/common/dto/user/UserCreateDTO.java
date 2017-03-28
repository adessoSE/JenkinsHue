package de.adesso.jenkinshue.common.dto.user;

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
public class UserCreateDTO implements Serializable {
	
	private static final long serialVersionUID = -8978663095485930946L;
	
	private String login;
	
	private long teamId;

}
