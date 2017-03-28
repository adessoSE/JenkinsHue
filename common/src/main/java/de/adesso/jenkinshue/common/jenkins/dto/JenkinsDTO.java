package de.adesso.jenkinshue.common.jenkins.dto;

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
public class JenkinsDTO implements Serializable {
	
	private static final long serialVersionUID = -9157030334676753856L;
	
	private List<JenkinsJobDTO> jobs;
	
	public static String getTreeParameter() {
		return "jobs[" + JenkinsJobDTO.getTreeParameter() + "]";
	}

}
