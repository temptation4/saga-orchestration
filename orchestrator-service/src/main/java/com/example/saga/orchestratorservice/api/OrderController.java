package com.example.saga.orchestratorservice.api;

import com.example.saga.common.Topics;
import com.example.saga.common.events.InventoryEvents;
import com.example.saga.common.events.OrderEvents;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final KafkaTemplate<String,Object> kafka;

    public OrderController(KafkaTemplate<String,Object> kafka){
        this.kafka = kafka;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestParam String userId,
                                         @RequestParam String itemName,
                                         @RequestParam int quantity,
                                         @RequestParam BigDecimal amount) {

        String orderId = OrderEvents.newOrderId();

        // 1) Emit OrderCreated
        kafka.send(Topics.ORDER_CREATED, new OrderEvents.OrderCreated(orderId, userId, itemName, quantity, amount));

        // 2) Ask Inventory to reserve
        kafka.send(Topics.INVENTORY_RESERVE, new InventoryEvents.ReserveInventory(orderId, itemName, quantity,amount));

        return ResponseEntity.ok(orderId);
    }
}
