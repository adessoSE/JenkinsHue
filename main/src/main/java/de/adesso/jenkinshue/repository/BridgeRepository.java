package de.adesso.jenkinshue.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.adesso.jenkinshue.entity.Bridge;

/**
 * 
 * @author wennier
 *
 */
@Repository
public interface BridgeRepository extends JpaRepository<Bridge, Long> {

	public static final String findBySearchItem = "SELECT b FROM Bridge b LEFT JOIN b.user u  WHERE LOWER(u.email) LIKE %:item% OR LOWER(b.ip) LIKE %:item%";

	Bridge findByIp(String ip);

	@Query("SELECT COUNT(b) FROM Bridge b LEFT JOIN b.user u  WHERE LOWER(u.email) LIKE %:item% OR LOWER(b.ip) LIKE %:item%")
	long count(@Param("item") String searchItem);

	/**
	 * 
	 * @param searchItem E-Mail oder IP enthaelt Suchbegriff
	 * @return
	 */
	@Query(BridgeRepository.findBySearchItem)
	List<Bridge> findBySearchItem(@Param("item") String searchItem);

	@Query(BridgeRepository.findBySearchItem)
	List<Bridge> findBySearchItem(@Param("item") String searchItem, Pageable pageable);

}
