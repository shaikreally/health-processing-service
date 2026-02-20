package com.example.health_processing_service.service;

import com.example.health_processing_service.domain.entity.ServiceStatusEntity;
import com.example.health_processing_service.repository.ServiceStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monitoring_event_contract.event.HealthEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthProcessingService {

    private final ServiceStatusRepository repository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${health.alert-topic}")
    private String ALERT_TOPIC;

    public void process(HealthEvent event) {

        Instant now = Instant.now();
        ServiceStatusEntity existing = repository.findById(event.getServiceInstanceId()).orElse(null);

        if (existing == null) {
            createNewStatus(event, now);
            return;
        }

        log.warn("Status : "+existing.getCurrentStatus().equals(event.getStatus()));

        // Service status changed from UP to DOWN or viceversa
        if (!existing.getCurrentStatus().equals(event.getStatus())) {
            publishAlert(event);
            existing.setLastUpdated(now);
            existing.setCurrentStatus(event.getStatus());
            log.warn("Service status changed from {} to {}", existing.getCurrentStatus(), event.getStatus());
        }
        existing.setLastHeartbeatAt(now);
        repository.save(existing);
    }

    private void createNewStatus(HealthEvent event, Instant now) {
        ServiceStatusEntity entity = new ServiceStatusEntity();
        entity.setServiceInstanceId(event.getServiceInstanceId());
        entity.setTenantId(event.getTenantId());
        entity.setServiceName(event.getServiceName());
        entity.setCurrentStatus(event.getStatus());
        entity.setLastUpdated(Instant.now());
        entity.setLastHeartbeatAt(now);
        repository.save(entity);
    }

    private void publishAlert(HealthEvent event) {
        kafkaTemplate.send(ALERT_TOPIC, event);
    }
}
