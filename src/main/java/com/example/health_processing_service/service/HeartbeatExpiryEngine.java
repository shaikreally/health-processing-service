package com.example.health_processing_service.service;

import com.example.health_processing_service.domain.entity.ServiceStatusEntity;
import com.example.health_processing_service.repository.ServiceStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monitoring_event_contract.event.HealthEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HeartbeatExpiryEngine {

    private final ServiceStatusRepository repository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${health.alert-topic}")
    private String ALERT_TOPIC;

    // heartbeat threshold in milliseconds (e.g. 3000)
    @Value("@{monitoring.heartbeat.expiry-threshold-ms}")
    private static long EXPIRY_THRESHOLD_MS;

    @Scheduled(fixedDelayString = "${monitoring.heartbeat.check-interval-ms:10000}")
    @Transactional
    public void checkExpiredHeartbeats() {
        Instant expiryCutoff = Instant.now().minusMillis(EXPIRY_THRESHOLD_MS);
        log.debug("Checking expired services with cutoff {}", expiryCutoff);

        List<ServiceStatusEntity> expiredServices = repository.findExpiredServices(expiryCutoff);

        for (ServiceStatusEntity sse : expiredServices) {
            markDownAndPublishAlert(sse);
        }
    }

    private void markDownAndPublishAlert(ServiceStatusEntity sse) {
        // Avoid duplicate state transitions
        if (!"UP".equals(sse.getCurrentStatus())) {
            return;
        }

        log.warn("Service expired: {} Marking DOWN", sse.getServiceInstanceId());
        sse.setCurrentStatus("DOWN");
        sse.setLastUpdated(Instant.now());

        repository.save(sse);

        // Create alert event
        HealthEvent alertEvent = new HealthEvent();
        alertEvent.setTenantId(sse.getTenantId());
        alertEvent.setServiceName(sse.getServiceName());
        alertEvent.setServiceInstanceId(sse.getServiceInstanceId());
        alertEvent.setStatus("DOWN");
        alertEvent.setTimestamp(Instant.now());

        kafkaTemplate.send(ALERT_TOPIC, alertEvent);
    }
}
