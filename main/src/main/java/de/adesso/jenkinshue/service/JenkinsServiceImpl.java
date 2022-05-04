package de.adesso.jenkinshue.service;

import de.adesso.jenkinshue.common.jenkins.dto.JenkinsDTO;
import de.adesso.jenkinshue.common.jenkins.dto.JenkinsFolderOrJobDTO;
import de.adesso.jenkinshue.common.jenkins.dto.JenkinsFolderStructureDTO;
import de.adesso.jenkinshue.common.jenkins.dto.JenkinsJobDTO;
import de.adesso.jenkinshue.common.jenkins.dto.JenkinsJobNamesDTO;
import de.adesso.jenkinshue.common.service.JenkinsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author wennier
 *
 */
@Slf4j
@Primary
@Service
public class JenkinsServiceImpl implements JenkinsService {

	@Value("${jenkins.url}")
	private String jenkinsUrl;
	
	@Value("${jenkins.userName}")
	private String userName;
	
	@Value("${jenkins.password}")
	private String password;
	
	private static final String API_JSON_TREE = "api/json?tree=";
	
	private RestTemplate template;
	
	@PostConstruct
	public void init() {
		template = new RestTemplate();
		template.getInterceptors().add(new BasicAuthenticationInterceptor(userName, password));
	}
	
	@Override
	public String getJenkinsUrl() {
		return jenkinsUrl;
	}
	
	@Override
	public JenkinsDTO getJenkins() {
		JenkinsFolderOrJobDTO folderOrJob = template.getForObject(jenkinsUrl + API_JSON_TREE + JenkinsFolderOrJobDTO.getTreeParameter(), JenkinsFolderOrJobDTO.class);
		return convert(folderOrJob);
	}
	
	@Override
	public JenkinsJobNamesDTO getJenkinsJobNames() {
		JenkinsFolderStructureDTO folderStructure = template.getForObject(jenkinsUrl + API_JSON_TREE + JenkinsFolderStructureDTO.getTreeParameter(), JenkinsFolderStructureDTO.class);
		if (folderStructure != null) {
			log.debug(folderStructure.toString());
		}
		JenkinsJobNamesDTO jobNames = convert(folderStructure);
		log.debug(jobNames.toString());
		return jobNames;
	}
	
	JenkinsDTO convert(JenkinsFolderOrJobDTO dto) {
		return new JenkinsDTO(getJobs(dto, null));
	}

	List<JenkinsJobDTO> getJobs(JenkinsFolderOrJobDTO dto, String folderName) {
		List<JenkinsJobDTO> jobs = new ArrayList<>();
		if (dto.getBuilds() != null) { // it is a job
			if(folderName == null || folderName.isEmpty()) {
				jobs.add(new JenkinsJobDTO(dto.getName(), dto.getBuilds()));
			} else {
				jobs.add(new JenkinsJobDTO(folderName + "/" + dto.getName(), dto.getBuilds()));
			}
		} else if (dto.getJobs() != null) { // it is a folder
			String subfolderName;
			if(folderName == null || folderName.isEmpty()) {
				subfolderName = dto.getName();
			} else {
				subfolderName = folderName + "/" + dto.getName();
			}
			for(JenkinsFolderOrJobDTO folderOrJob : dto.getJobs()) {
				jobs.addAll(getJobs(folderOrJob, subfolderName));
			}
		}
		return jobs;
	}
	
	JenkinsJobNamesDTO convert(JenkinsFolderStructureDTO dto) {
		return new JenkinsJobNamesDTO(getJobNames(dto, null));
	}
	
	List<JenkinsJobNamesDTO.JenkinsJobNamesDTO_JobDTO> getJobNames(JenkinsFolderStructureDTO dto, String folderName) {
		List<JenkinsJobNamesDTO.JenkinsJobNamesDTO_JobDTO> jobNames = new ArrayList<>();
		if(dto.getJobs() == null) { // it is a job
			if(folderName == null || folderName.isEmpty()) {
				jobNames.add(new JenkinsJobNamesDTO.JenkinsJobNamesDTO_JobDTO(dto.getName()));
			} else {
				jobNames.add(new JenkinsJobNamesDTO.JenkinsJobNamesDTO_JobDTO(folderName + "/" + dto.getName()));
			}
		} else { // it is a folder
			String subfolderName;
			if(folderName == null || folderName.isEmpty()) {
				subfolderName = dto.getName();
			} else {
				subfolderName = folderName + "/" + dto.getName();
			}
			for(JenkinsFolderStructureDTO folderOrJob : dto.getJobs()) {
				jobNames.addAll(getJobNames(folderOrJob, subfolderName));
			}
		}
		return jobNames;
	}

}
