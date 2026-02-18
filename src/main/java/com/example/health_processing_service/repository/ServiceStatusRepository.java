package com.example.health_processing_service.repository;

import com.example.health_processing_service.domain.entity.ServiceStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ServiceStatusRepository extends JpaRepository<ServiceStatusEntity, String> {

    @Query("SELECT s FROM service_status s WHERE s.currentStatus = 'UP' AND s.lastHeartbeatAt < :expiryTime")
    List<ServiceStatusEntity> findExpiredServices(@Param("expiryTime") Instant expiryTime);
}
