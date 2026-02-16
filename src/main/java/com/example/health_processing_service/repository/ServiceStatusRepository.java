package com.example.health_processing_service.repository;

import com.example.health_processing_service.domain.entity.ServiceStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceStatusRepository extends JpaRepository<ServiceStatusEntity, String> {

}
