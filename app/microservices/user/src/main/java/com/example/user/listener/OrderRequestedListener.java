package com.example.user.listener;

import com.example.user.event.OrderRequested;
import com.example.user.event.OrderUserValidated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class OrderRequestedListener {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "order.requested", groupId = "user-service")
    public void listen(ConsumerRecord<String, OrderRequested> record) {
        log.info("Received message from 'order.requested': key={}, payload={}", record.key(), record.value());

        OrderRequested event = record.value();

        boolean valid = validateUser(event.userId());

        String reason = valid ? null : "User not found";

        OrderUserValidated response = new OrderUserValidated(event.orderId(), valid, reason);
        kafkaTemplate.send("order.user-validated", event.orderId(), response)
                .whenComplete((result, exp) -> {
                    if (exp != null) {
                        log.error("❌ Failed to send to 'order.product-validated': {}", response, exp);
                    } else {
                        log.info("✅ Successfully sent to 'order.product-validated': {}", response);
                    }
                });
    }

    private boolean validateUser(String userId) {
        // Replace with real logic (DB lookup etc.)
        return true;
    }

}

