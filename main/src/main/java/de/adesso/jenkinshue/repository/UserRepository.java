package de.adesso.jenkinshue.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.adesso.jenkinshue.entity.User;

/**
 * 
 * @author wennier
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	String FIND_BY_SEARCH_ITEM = "SELECT u FROM User u LEFT JOIN u.team t WHERE LOWER(u.forename) LIKE %:item% OR LOWER(u.surname) LIKE %:item% OR LOWER(t.name) LIKE %:item%";
	
	@Query("SELECT u FROM User u LEFT JOIN FETCH u.bridges b WHERE u.login = :login")
	User findByLogin(@Param("login") String login);
	
	@Query(UserRepository.FIND_BY_SEARCH_ITEM)
	List<User> findBySearchItem(@Param("item") String searchItem);

	@Query(UserRepository.FIND_BY_SEARCH_ITEM)
	List<User> findBySearchItem(@Param("item") String searchItem, Pageable pageable);
	
	@Query("SELECT u FROM User u LEFT JOIN FETCH u.bridges b WHERE u.team.id = :teamId")
	List<User> findAllOfTeam(@Param("teamId") long teamId);
	
	@Query("SELECT COUNT(u) FROM User u WHERE u.team.id = :teamId")
	long count(@Param("teamId") long teamId);
	
	@Query("SELECT COUNT(u) FROM User u LEFT JOIN u.team t WHERE LOWER(u.forename) LIKE %:item% OR LOWER(u.surname) LIKE %:item% OR LOWER(t.name) LIKE %:item%")
	long count(@Param("item") String searchItem);
	
}
