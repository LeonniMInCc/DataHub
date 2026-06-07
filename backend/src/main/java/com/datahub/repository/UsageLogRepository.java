package com.datahub.repository;

import com.datahub.entity.UsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UsageLogRepository extends JpaRepository<UsageLog, Long> {

    List<UsageLog> findBySubId(Long subId);

    long countByRequestTimeAfter(LocalDateTime after);

    @Query("SELECT FUNCTION('DATE', u.requestTime), COUNT(u) FROM UsageLog u " +
           "WHERE u.requestTime >= :start GROUP BY FUNCTION('DATE', u.requestTime) ORDER BY FUNCTION('DATE', u.requestTime)")
    List<Object[]> countByDaySince(@Param("start") LocalDateTime start);

    @Query("SELECT COUNT(u) FROM UsageLog u WHERE u.requestTime >= :start AND u.statusCode = 200")
    long countSuccessfulSince(@Param("start") LocalDateTime start);

    @Query("SELECT FUNCTION('DATE', u.requestTime), COUNT(u) FROM UsageLog u " +
           "GROUP BY FUNCTION('DATE', u.requestTime) ORDER BY FUNCTION('DATE', u.requestTime) DESC")
    List<Object[]> countGroupByDay();
}
