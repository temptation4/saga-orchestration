package com.example.saga.orderservice.api;

import com.example.saga.orderservice.store.OrderStore;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderQueryController {
    private final OrderStore store;
    public OrderQueryController(OrderStore store){ this.store = store; }

    @GetMapping("/{orderId}")
    public String status(@PathVariable("orderId") String orderId){
        var s = store.get(orderId);
        return s == null ? "UNKNOWN" : s.name();
    }

    @GetMapping
    public Map<String, OrderStore.Status> all(){ return store.all(); }
}
