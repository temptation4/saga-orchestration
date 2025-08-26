package com.example.saga.common.events;

import java.math.BigDecimal;

public class InventoryEvents {
    public record ReserveInventory(String orderId, String itemName, int quantity, BigDecimal amount) {}
    public record InventoryReserved(String orderId, String itemName, int quantity, BigDecimal amount) {}
    public record InventoryFailed(String orderId, String reason) {}
    public record ReleaseInventory(String orderId, String itemName, int quantity) {}
}
