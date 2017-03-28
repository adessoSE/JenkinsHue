package de.adesso.jenkinshue.common.service;

import de.adesso.jenkinshue.common.dto.job.JobDTO;

/**
 * 
 * @author wennier
 *
 */
public interface JobService {
	
	JobDTO update(JobDTO job);
	
	long countNameDistinctly(long teamId);
	
	long countNameDistinctly();
	
}
