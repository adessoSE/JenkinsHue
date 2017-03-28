package de.adesso.jenkinshue.common.dto;

import java.io.Serializable;
import java.security.Principal;

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
public class PrincipalDTO implements Serializable {
	
	private static final long serialVersionUID = 2531641403888873781L;
	
	private Principal principal;
	
	private long userId;
	
	private long teamId;

}
