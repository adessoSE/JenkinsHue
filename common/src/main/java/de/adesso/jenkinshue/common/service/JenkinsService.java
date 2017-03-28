package de.adesso.jenkinshue.common.service;

import de.adesso.jenkinshue.common.jenkins.dto.JenkinsDTO;
import de.adesso.jenkinshue.common.jenkins.dto.JenkinsJobNamesDTO;

/**
 * 
 * @author wennier
 *
 */
public interface JenkinsService {
	
	String getJenkinsUrl();
	
	JenkinsDTO getJenkins();
	
	JenkinsJobNamesDTO getJenkinsJobNames();
	
}
