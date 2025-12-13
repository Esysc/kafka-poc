package com.example.claims.controller;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import com.example.claims.model.Claim;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;


/**
 * REST controller for topic log viewing.
 * <p>
 * This class is not intended for extension.
 */
@RestController
public final class TopicLogController {

    /** Maximum number of logs to keep. */
    private static final int MAX_LOGS = 100;
    /** List of claim logs. */
    private final List<Claim> logs = new ArrayList<>();

    /**
     * Returns the current topic log.
     *
     * @return the list of claims in the log
     */
    @GetMapping("/api/topic-log")
    public List<Claim> getTopicLog() {
        return logs;
    }

    /**
     * Kafka listener for the "claims-input" topic.
     *
     * @param record the consumed record
     */
    @KafkaListener(
        topics = "claims-input",
        groupId = "log-viewer",
        containerFactory = "claimKafkaListenerContainerFactory"
    )
    public void listen(final ConsumerRecord<String, Claim> record) {
        logs.add(record.value());
        if (logs.size() > MAX_LOGS) {
            logs.remove(0); // Keep only the last MAX_LOGS messages
        }
    }
}
