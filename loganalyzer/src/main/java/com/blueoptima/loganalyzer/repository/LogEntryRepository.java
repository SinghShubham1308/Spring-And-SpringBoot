package com.blueoptima.loganalyzer.repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blueoptima.loganalyzer.dto.IpCountDto;
import com.blueoptima.loganalyzer.entity.LogEntry;



@Repository
public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {

	List<LogEntry> findByEnterpriseIdAndTimestampBetween(Long enterpriseId, Instant from, Instant to);

	@Query("SELECT COUNT(DISTINCT l.api) FROM LogEntry l WHERE l.userLogin = :userLogin")
	long countDistinctApisByUser(@Param("userLogin") String userLogin);

	@Query(value = "SELECT ip_address, COUNT(*) as cnt FROM logs_db.log_entries "
			+ "WHERE timestamp >= :start AND timestamp < :end "
			+ "GROUP BY ip_address ORDER BY cnt DESC LIMIT 10", nativeQuery = true)
	List<IpCountDto> topIpsForDay(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	List<LogEntry> findByStatusCodeGreaterThanEqualAndEnterpriseIdAndTimestampBetween(int status, Long enterpriseId,
			Instant from, Instant to);
	
    @Transactional
    long deleteByTimestampBefore(Instant cutoffDate);

}
