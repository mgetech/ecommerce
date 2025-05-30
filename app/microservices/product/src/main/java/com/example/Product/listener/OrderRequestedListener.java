package com.example.Product.listener;

import com.example.Product.event.OrderProductValidated;
import com.example.Product.event.OrderRequested;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderRequestedListener {

    private final KafkaTemplate<Long, Object> kafkaTemplate;


    @KafkaListener(topics = "order.requested", groupId = "product-service")
    public void listen(ConsumerRecord<Long, OrderRequested> record) {
        OrderRequested event = record.value();

        boolean valid = validateProduct(event.productId());
        String reason = valid ? null : "Product not found";

        OrderProductValidated response = new OrderProductValidated(event.orderId(), valid, reason);
        kafkaTemplate.send("order.product-validated", event.orderId(), response);
    }

    private boolean validateProduct(Long productId) {
        // Replace with real logic (DB lookup etc.)
        return productId != null && productId > 0;
    }
}