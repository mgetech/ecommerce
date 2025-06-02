package com.example.order.service;


import com.example.order.config.KafkaConsumerConfig;
import com.example.order.dto.OrderRequestDTO;
import com.example.order.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.example.order.entity.OrderStatus.CONFIRMED;
import static com.example.order.entity.OrderStatus.FAILED;

@ActiveProfiles("test")
@SpringBootTest
@Rollback(false)
public class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    @Test
    public void testUpdateStatus(){
        System.out.println(orderServiceImpl.updateStatus(1l,FAILED));

    }

    @Test
    public void testCreateOrderSimple(){
        OrderRequestDTO dto = new OrderRequestDTO(5L,1L,73);
        System.out.println(orderServiceImpl.createSimpleOrder(dto));

    }
}
