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
public class JenkinsJobNamesDTO implements Serializable {
	
	private static final long serialVersionUID = -4766925398358252521L;
	
	private List<JobDTO> jobs;
	
	public static String getTreeParameter() {
		return "jobs[name]";
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class JobDTO implements Serializable {

		private static final long serialVersionUID = -3362555047912118372L;
		
		private String name;

	}

}
