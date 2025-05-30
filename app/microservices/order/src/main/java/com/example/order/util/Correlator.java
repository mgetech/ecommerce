package com.example.order.util;

import com.example.order.event.OrderFinalized;
import com.example.order.event.OrderProductValidated;
import com.example.order.event.OrderUserValidated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class Correlator {

    private record State(boolean userOk, boolean prodOk, String reason) {}

    private final Map<Long, State> states = new ConcurrentHashMap<>();

    Map<Long, Sinks.One<OrderFinalized>> sinks = new ConcurrentHashMap<>();

    public Mono<OrderFinalized> registerMono(Long orderId) {
        Sinks.One<OrderFinalized> sink = Sinks.one();
        sinks.put(orderId, sink);
        return sink.asMono();
    }


    @KafkaListener(topics = "order.user-validated", groupId = "order-service")
    public void onUserValidated(OrderUserValidated event) {
        handle(event.orderId(), event.ok(), event.reason(), true);
    }

    @KafkaListener(topics = "order.product-validated", groupId = "order-service")
    public void onProductValidated(OrderProductValidated event) {
        handle(event.orderId(), event.ok(), event.reason(), false);
    }

    private void handle(Long id, boolean ok, String reason, boolean isUser) {
        states.merge(id,
                new State(isUser ? ok : false, isUser ? false : ok, reason),
                (old, nxt) -> new State(
                        isUser ? ok : old.userOk,
                        isUser ? old.prodOk : ok,
                        !ok ? reason : old.reason));

        State s = states.get(id);
        if ((s.userOk && s.prodOk) || (!s.userOk || !s.prodOk)) {
            boolean confirmed = s.userOk && s.prodOk;
            OrderFinalized finalEvt = new OrderFinalized(id, confirmed, confirmed ? null : s.reason);
            if (sinks.containsKey(id)) {
                sinks.remove(id).tryEmitValue(finalEvt);
            }
        }
    }
}