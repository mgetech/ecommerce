package com.example.order.config;

import com.example.order.event.OrderProductValidated;
import com.example.order.event.OrderUserValidated;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaConsumerConfig {

    // Common consumer properties map (sharing bootstrap, group, trusted packages, etc.)
    private Map<String, Object> baseConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "order-service");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Wrap the real deserializers in an ErrorHandlingDeserializer:
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        // Tell the ErrorHandlingDeserializer which delegate to use:
//        props.put("spring.deserializer.key.delegate.class", StringDeserializer.class);
        // We will override the “value.delegate.class” in each factory below

//        props.put(JsonDeserializer.REMOVE_TYPE_INFO_HEADERS, "true");
        return props;
    }

    @Bean
    public ConsumerFactory<String, OrderUserValidated> userValidatedConsumerFactory() {
        log.info("Creating UserValidatedConsumerFactory with trusted packages: com.example.order.event, com.example.user.event");
        var configs = baseConsumerConfigs();
        // Delegate the value deserializer to JsonDeserializer<OrderUserValidated>
//        configs.put("spring.deserializer.key.delegate.class", StringDeserializer.class);
//        configs.put("spring.deserializer.value.delegate.class", JsonDeserializer.class);

        var deserializer = new JsonDeserializer<>(OrderUserValidated.class);
        deserializer.setUseTypeHeaders(false);

//        deserializer.addTrustedPackages("com.example.order.event");
        deserializer.addTrustedPackages(
                "com.example.order.event",
                "com.example.user.event"
        );
        return new DefaultKafkaConsumerFactory<>(
                configs,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderUserValidated> userValidatedListenerContainer(
            ConsumerFactory<String, OrderUserValidated> cf) {
        ConcurrentKafkaListenerContainerFactory<String, OrderUserValidated> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, OrderProductValidated> productValidatedConsumerFactory() {
        log.info("Creating ProductValidatedConsumerFactory with trusted packages: com.example.order.event, com.example.Product.event");
        var configs = baseConsumerConfigs();
//        configs.put("spring.deserializer.key.delegate.class", StringDeserializer.class);
//        configs.put("spring.deserializer.value.delegate.class", JsonDeserializer.class);
        var deserializer = new JsonDeserializer<>(OrderProductValidated.class);
        deserializer.setUseTypeHeaders(false);
//        deserializer.addTrustedPackages("com.example.order.event");
        deserializer.addTrustedPackages(
                "com.example.order.event",
                "com.example.Product.event"
        );
        return new org.springframework.kafka.core.DefaultKafkaConsumerFactory<>(
                configs,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderProductValidated> productValidatedListenerContainer(
            ConsumerFactory<String, OrderProductValidated> cf) {
        ConcurrentKafkaListenerContainerFactory<String, OrderProductValidated> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        return factory;
    }
}
