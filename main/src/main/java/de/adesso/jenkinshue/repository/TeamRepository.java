package de.adesso.jenkinshue.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.adesso.jenkinshue.entity.Team;

/**
 * 
 * @author wennier
 *
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
	
	public static final String FIND_BY_SEARCH_ITEM = "SELECT t FROM Team t WHERE LOWER(t.name) LIKE %:item%";

	Team findByName(String name);
	
	@Query(TeamRepository.FIND_BY_SEARCH_ITEM)
	List<Team> findBySearchItem(@Param("item") String searchItem);
	
	@Query(TeamRepository.FIND_BY_SEARCH_ITEM)
	List<Team> findBySearchItem(@Param("item") String searchItem, Pageable pageable);
	
	@Query("SELECT COUNT(t) FROM Team t WHERE LOWER(t.name) LIKE %:item%")
	long count(@Param("item") String searchItem);
	
}
