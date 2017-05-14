package de.adesso.jenkinshue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import de.adesso.jenkinshue.common.dto.job.JobDTO;
import de.adesso.jenkinshue.common.service.JobService;
import de.adesso.jenkinshue.dozer.Mapper;
import de.adesso.jenkinshue.entity.Job;
import de.adesso.jenkinshue.repository.JobRepository;

/**
 * 
 * @author wennier
 *
 */
@Primary
@Service
public class JobServiceImpl implements JobService {

	private final JobRepository jobRepository;
	
	private final Mapper mapper;

	@Autowired
	public JobServiceImpl(JobRepository jobRepository, Mapper mapper) {
		this.jobRepository = jobRepository;
		this.mapper = mapper;
	}
	
	@Override
	public JobDTO update(JobDTO job) {
		Job j = mapper.map(job, Job.class);
		j = jobRepository.save(j);
		return mapper.map(j, JobDTO.class);
	}

	@Override
	public long countNameDistinctly(long teamId) {
		return jobRepository.countNameDistinctly(teamId);
	}

	@Override
	public long countNameDistinctly() {
		return jobRepository.countNameDistinctly();
	}

}
