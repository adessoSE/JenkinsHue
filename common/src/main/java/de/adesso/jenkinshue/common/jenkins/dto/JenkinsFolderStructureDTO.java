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
public class JenkinsFolderStructureDTO implements Serializable {
	
	private static final long serialVersionUID = 8042296095186951693L;

	private String name;
	
	private List<JenkinsFolderStructureDTO> jobs;
	
	public static String getTreeParameter() {
		return getTree(JenkinsConstants.DEPTH);
	}
	
	private static String getTree(int depth) {
		if(depth > 0) {
			return "jobs[name," + getTree(depth - 1) + "]";
		} else {
			return "jobs[name]";
		}
	}

}
