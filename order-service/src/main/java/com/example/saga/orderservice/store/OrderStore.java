package com.example.saga.orderservice.store;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderStore {
    public enum Status { CREATED, CANCELLED, CONFIRMED }
    private final Map<String, Status> orders = new ConcurrentHashMap<>();

    public void set(String orderId, Status status){ orders.put(orderId, status); }
    public Status get(String orderId){ return orders.get(orderId); }
    public Map<String, Status> all(){ return orders; }
}
