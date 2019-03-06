package de.adesso.jenkinshue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import de.adesso.jenkinshue.common.service.JobService;
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

	@Autowired
	public JobServiceImpl(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
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
