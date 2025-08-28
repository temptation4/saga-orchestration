package com.example.saga.orderservice.api;

import com.example.saga.common.Topics;
import com.example.saga.common.events.OrderEvents;
import com.example.saga.orderservice.dto.OrderRequest;
import com.example.saga.orderservice.entity.Orders;
import com.example.saga.orderservice.repository.OrderRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderCommandController {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderCommandController(OrderRepository orderRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public String createOrder(@RequestBody OrderRequest request) {
        // Generate an orderId
        String orderId = UUID.randomUUID().toString();

        // Publish OrderCreatedEvent to Kafka
        OrderEvents.OrderCreated event = new OrderEvents.OrderCreated(
                orderId,
                request.customerId(),
                request.productId(),
                request.quantity(),
                request.price()
        );
        kafkaTemplate.send(Topics.ORDER_CREATED, orderId, event);

        return orderId;
    }
}
