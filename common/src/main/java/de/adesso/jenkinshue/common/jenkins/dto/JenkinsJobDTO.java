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
public class JenkinsJobDTO implements Serializable {
	
	private static final long serialVersionUID = -4503229749561335420L;
	
	private String name;
	
	private List<JenkinsBuildDTO> builds;
	
	public static String getTreeParameter() {
		return "name,builds[" + JenkinsBuildDTO.getTreeParameter() + "]";
	}
	
}
