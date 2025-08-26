package com.example.saga.common.events;

import java.math.BigDecimal;

public class PaymentEvents {
    public record ProcessPayment(String orderId, String name, BigDecimal amount,int quantity) {}
    public record PaymentProcessed(String orderId) {}
    public record PaymentFailed(String orderId,String name,int quantity, String reason) {}
    public record RefundPayment(String orderId) {}
}
