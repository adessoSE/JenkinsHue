package de.adesso.jenkinshue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.adesso.jenkinshue.entity.Job;

/**
 * 
 * @author wennier
 *
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
	
	@Query("SELECT COUNT(DISTINCT j.name) FROM Lamp l LEFT JOIN l.jobs j WHERE l.team.id = :teamId")
	long countNameDistinctly(@Param("teamId") long teamId);
	
	@Query("SELECT COUNT(DISTINCT j.name) FROM Job j")
	long countNameDistinctly();
	
}
