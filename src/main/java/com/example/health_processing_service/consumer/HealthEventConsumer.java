package com.example.health_processing_service.consumer;

import com.example.health_processing_service.service.HealthProcessingService;
import monitoring_event_contract.event.HealthEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HealthEventConsumer {

    private final HealthProcessingService processingService;

    @KafkaListener(topics = "health-reports", groupId = "health-processing-group")
    public void consume(HealthEvent event) {
        processingService.process(event);
    }
}
