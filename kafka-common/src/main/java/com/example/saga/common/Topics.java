package com.example.saga.common;

public final class Topics {
    public static final String ORDER_CREATED = "order-created";
    public static final String ORDER_CANCELLED = "order-cancelled";

    public static final String ORDER_COMPLETED = "order-completed";

    public static final String INVENTORY_RESERVE = "inventory-reserve";
    public static final String INVENTORY_RESERVED = "inventory-reserved";
    public static final String INVENTORY_FAILED = "inventory-failed";
    public static final String INVENTORY_RELEASE = "inventory-release";
    public static final String PAYMENT_PROCESS = "payment-process";
    public static final String PAYMENT_PROCESSED = "payment-processed";
    public static final String PAYMENT_FAILED = "payment-failed";
    public static final String PAYMENT_REFUND = "payment-refund";

    public static final String SHIPPING_SCHEDULE = "shipping-schedule";
    public static final String SHIPPING_SCHEDULED = "shipping-scheduled";
    public static final String SHIPPING_FAILED = "shipping-failed";

    private Topics(){}
}
