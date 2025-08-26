package com.example.saga.common.events;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderEvents {
    public record OrderConfirmed(String orderId, String userId, String itemName, int quantity, BigDecimal amount) {}
    public record OrderCreated(String orderId, String userId, String itemName, int quantity, BigDecimal amount) {}
    public record OrderCancelled(String orderId, String reason) {}

    public record OrderCompleted(String orderId, String reason) {}

    public static String newOrderId() { return UUID.randomUUID().toString(); }
}
