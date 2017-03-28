package de.adesso.jenkinshue.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.adesso.jenkinshue.entity.Lamp;

/**
 * 
 * @author wennier
 *
 */
@Repository
public interface LampRepository extends JpaRepository<Lamp, Long> {

//	@Query("SELECT l FROM Lamp l LEFT JOIN l.scenarioConfigs c LEFT JOIN l.team.scenarioPriority p LEFT JOIN l.jobs j WHERE l.id = :id")
//	Lamp findOne(@Param("id") long id);
	
	Lamp findByHueUniqueId(String hueUniqueId);
	
	@Query("SELECT l.hueUniqueId FROM Lamp l")
	List<String> findAllHueUniqueIds();

	@Query("SELECT COUNT(l) FROM Lamp l WHERE l.team.id = :teamId")
	long count(@Param("teamId") long teamId);
	
}
