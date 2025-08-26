package com.example.saga.orderservice.api;

import com.example.saga.orderservice.dto.OrderCreatedEvent;
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

        // Save in DB
        Orders order = new Orders();
        order.setOrderId(orderId);
        order.setQuantity(request.quantity());
        order.setAmount(Double.parseDouble(request.price().toString()));
        order.setName("");
        order.setStatus("PENDING");
        orderRepository.save(order);

        // Publish OrderCreatedEvent to Kafka
        OrderCreatedEvent event = new OrderCreatedEvent(
                orderId,
                request.customerId(),
                request.productId(),
                request.quantity(),
                request.price()
        );
        kafkaTemplate.send("order-created", orderId, event);

        return orderId;
    }
}
