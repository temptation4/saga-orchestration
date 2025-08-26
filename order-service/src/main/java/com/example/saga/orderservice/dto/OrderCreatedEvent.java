package com.example.saga.orderservice.dto;

import java.math.BigDecimal;

public record OrderCreatedEvent(String orderId, String customerId, String productId, int quantity, BigDecimal price) {}
