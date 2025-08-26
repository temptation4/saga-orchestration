package com.example.saga.inventoryservice.api;

import com.example.saga.inventoryservice.store.StockStore;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class StockController {
    private final StockStore store;
    public StockController(StockStore store){ this.store = store; }

    @GetMapping
    public Map<String,Integer> snapshot(){ return store.snapshot(); }
}
