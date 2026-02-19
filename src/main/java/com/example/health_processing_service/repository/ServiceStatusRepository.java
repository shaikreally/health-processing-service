package com.example.health_processing_service.repository;

import com.example.health_processing_service.domain.entity.ServiceStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ServiceStatusRepository extends JpaRepository<ServiceStatusEntity, String> {

    long countByCurrentStatus(String currentStatus);

    long countByTenantIdAndCurrentStatus(String tenantId, String currentStatus);

    @Query("SELECT DISTINCT s.tenantId FROM ServiceStatusEntity s")
    List<String> findDistinctTenantIds();

    @Query("SELECT s FROM ServiceStatusEntity s WHERE s.currentStatus = 'UP' AND s.lastHeartbeatAt < :expiryTime")
    List<ServiceStatusEntity> findExpiredServices(@Param("expiryTime") Instant expiryTime);
}
