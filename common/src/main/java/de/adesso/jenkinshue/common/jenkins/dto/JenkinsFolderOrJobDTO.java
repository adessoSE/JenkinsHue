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
public class JenkinsFolderOrJobDTO implements Serializable {
	
	private static final long serialVersionUID = -1104881461046680646L;
	
	private String name;
	
	private List<JenkinsBuildDTO> builds;
	
	private List<JenkinsFolderOrJobDTO> jobs;
	
	public static String getTreeParameter() {
		return getTree(JenkinsConstants.DEPTH);
	}
	
	private static String getTree(int depth) {
		String builds = "builds[building,result,number,timestamp]";
		if(depth > 0) {
			return "jobs[name," + builds + "," + getTree(depth - 1) + "]";
		} else {
			return "jobs[name," + builds + "]";
		}
	}
	
}

