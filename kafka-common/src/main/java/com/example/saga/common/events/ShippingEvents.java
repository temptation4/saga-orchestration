package com.example.saga.common.events;

public class ShippingEvents {
    public record ScheduleShipping(String orderId, String itemName, int quantity) {}
    public record ShippingScheduled(String orderId, String trackingId) {}
    public record ShippingFailed(String orderId, String itemName, int quantity, String reason) {}
}
