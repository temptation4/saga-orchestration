package com.example.saga.orderservice.listeners;

import com.example.saga.orderservice.entity.Orders;
import com.example.saga.orderservice.repository.OrderRepository;
import com.example.saga.common.events.OrderEvents;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderListeners {

    private final OrderRepository orderRepo;

    public OrderListeners(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @KafkaListener(topics = "order-created", groupId = "order-service")
    @Transactional("kafkaTransactionManager") // ✅ Kafka + DB atomic commit
    public void handleOrderCreated(OrderEvents.OrderCreated event) {
        Orders order = new Orders();
        String orderId = UUID.randomUUID().toString();
        order.setOrderId(orderId);
        order.setName(event.itemName());
        order.setQuantity(event.quantity());
        order.setAmount(Double.parseDouble(event.amount().toString()));
        order.setStatus("CREATED");
        orderRepo.save(order);
        System.out.println("✅ Order saved in DB: " + order.getOrderId());
    }

    @KafkaListener(topics = "order-cancelled", groupId = "order-service")
    public void handleOrderCancelled(OrderEvents.OrderCancelled event) {
        orderRepo.findById(event.orderId()).ifPresent(order -> {
            order.setStatus("CANCELLED");
            orderRepo.save(order);
        });
    }

    @KafkaListener(topics = "order-confirmed", groupId = "order-service")
    public void handleOrderConfirmed(OrderEvents.OrderConfirmed event) {
        orderRepo.findById(event.orderId()).ifPresent(order -> {
            order.setStatus("CONFIRMED");
            orderRepo.save(order);
        });
    }
}
