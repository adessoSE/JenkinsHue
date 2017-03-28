package de.adesso.jenkinshue.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.adesso.jenkinshue.common.jenkins.dto.JenkinsDTO;
import de.adesso.jenkinshue.common.jenkins.dto.JenkinsJobNamesDTO;
import de.adesso.jenkinshue.common.service.JenkinsService;

/**
 * 
 * @author wennier
 *
 */
@RestController
@RequestMapping("/rest/jenkins")
public class JenkinsController implements JenkinsService {

	@Autowired
	private JenkinsService jenkinsService;
	
	@Override
	@RequestMapping(value = "/url", produces="text/plain")
	public String getJenkinsUrl() {
		return jenkinsService.getJenkinsUrl();
	}
	
	@Override
	public JenkinsDTO getJenkins() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	@RequestMapping("/jobs")
	public JenkinsJobNamesDTO getJenkinsJobNames() {
		return jenkinsService.getJenkinsJobNames();
	}

}
