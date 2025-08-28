package com.example.saga.inventoryservice.listeners;

import com.example.saga.common.Topics;
import com.example.saga.inventoryservice.entity.Inventory;
import com.example.saga.inventoryservice.repository.InventoryRepository;
import com.example.saga.common.events.InventoryEvents;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryListeners {

    private final InventoryRepository repo;
    private final KafkaTemplate<String, Object> kafka;

    public InventoryListeners(InventoryRepository repo, KafkaTemplate<String, Object> kafka) {
        this.repo = repo;
        this.kafka = kafka;
    }

    @KafkaListener(topics = Topics.INVENTORY_RESERVE, groupId = "inventory-service")
    public void handleReserve(InventoryEvents.ReserveInventory event) {
        Inventory item = repo.findById(event.orderId()).orElse(null);

        if (item != null && item.getQuantity() >= event.quantity()) {
            item.setQuantity(item.getQuantity() - event.quantity());
            item.setStatus("RESERVED");
            item.setName("Mobile");
            item.setOrderId(item.getOrderId());
            repo.save(item);
            kafka.send("inventory-reserved", new InventoryEvents.InventoryReserved(event.orderId(), event.itemName(), event.quantity(),event.amount()));
            System.out.println("✅ Reserved inventory for order: " + event.orderId());
        } else {
            kafka.send("inventory-failed", new InventoryEvents.InventoryFailed(event.orderId(), "❌ Inventory not available for order"));
            System.out.println("❌ Inventory not available for order: " + event.orderId());
        }
    }
}
