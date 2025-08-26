package com.example.saga.orderservice.dto;

import java.math.BigDecimal;

public record OrderRequest(String customerId, String productId, int quantity, BigDecimal price) {}
