package com.example.user.listener;

import com.example.user.event.OrderRequested;
import com.example.user.event.OrderUserValidated;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderRequestedListener {

    private final KafkaTemplate<Long, Object> kafkaTemplate;

    @KafkaListener(topics = "order.requested", groupId = "user-service")
    public void listen(ConsumerRecord<Long, OrderRequested> record) {
        OrderRequested event = record.value();

        boolean valid = validateUser(event.userId());
        String reason = valid ? null : "User not found";

        OrderUserValidated response = new OrderUserValidated(event.orderId(), valid, reason);
        kafkaTemplate.send("order.user-validated", event.orderId(), response);
    }

    private boolean validateUser(Long userId) {
        // Replace with real logic (DB lookup etc.)
        return userId != null && userId > 0;
    }

}

