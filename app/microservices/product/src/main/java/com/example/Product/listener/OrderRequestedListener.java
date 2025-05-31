package com.example.Product.listener;

import com.example.Product.event.OrderProductValidated;
import com.example.Product.event.OrderRequested;
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


    @KafkaListener(topics = "order.requested", groupId = "product-service")
    public void listen(ConsumerRecord<String, OrderRequested> record) {
        log.info("Received message from 'order.requested': key={}, payload={}", record.key(), record.value());
        OrderRequested event = record.value();

        boolean valid = validateProduct(event.productId());

        String reason = valid ? null : "Product not found";

        OrderProductValidated response = new OrderProductValidated(event.orderId(), valid, reason);
        kafkaTemplate.send("order.product-validated", event.orderId(), response)
                .whenComplete((result, exp) -> {
                    if (exp != null) {
                        log.error("❌ Failed to send to 'order.product-validated': {}", response, exp);
                    } else {
                        log.info("✅ Successfully sent to 'order.product-validated': {}", response);
                    }
                });
    }

    private boolean validateProduct(String productId) {
        // Replace with real logic (DB lookup etc.)
        return true;
    }
}