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

    private record State(Boolean userOk, Boolean prodOk, String reason) {}

    private final Map<String, State> states = new ConcurrentHashMap<>();

    Map<String, Sinks.One<OrderFinalized>> sinks = new ConcurrentHashMap<>();

    public Mono<OrderFinalized> registerMono(String orderId) {
        Sinks.One<OrderFinalized> sink = Sinks.one();
        sinks.put(orderId, sink);
        return sink.asMono();
    }


    @KafkaListener(topics = "order.user-validated", containerFactory = "userValidatedListenerContainer")
    public void onUserValidated(OrderUserValidated event) {

        handle(event.orderId(), event.ok(), event.reason(), true);
    }

    @KafkaListener(topics = "order.product-validated", containerFactory = "productValidatedListenerContainer")
    public void onProductValidated(OrderProductValidated event) {
        handle(event.orderId(), event.ok(), event.reason(), false);
    }

    private void handle(String id, boolean ok, String reason, boolean isUser) {
        states.merge(id,
                // if no existing State: initialize one side to ok, the other to null
                new State(isUser ? ok : null, isUser ? null : ok, reason),
                (old, nxt) -> {
                    Boolean newUserOk = isUser ? ok : old.userOk;
                    Boolean newProdOk = isUser ? old.prodOk : ok;
                    String  newReason = !ok ? reason : old.reason;
                    return new State(newUserOk, newProdOk, newReason);
                }
        );

        State s = states.get(id);
        // Only finalize when both sides are non-null (i.e. we've gotten both events)
        if (s.userOk != null && s.prodOk != null) {
            boolean confirmed = s.userOk && s.prodOk;
            OrderFinalized finalEvt = new OrderFinalized(id, confirmed, confirmed ? null : s.reason);
            if (sinks.containsKey(id)) {
                sinks.remove(id).tryEmitValue(finalEvt);
            }
            // Optionally: cleanup `states.remove(id)` if you don’t need to track it anymore
        }
    }

}