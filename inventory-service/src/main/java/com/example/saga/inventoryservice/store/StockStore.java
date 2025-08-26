package com.example.saga.inventoryservice.store;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StockStore {
    private final Map<String, Integer> stock = new ConcurrentHashMap<>();

    public StockStore(){
        stock.put("item", 5);
    }

    public synchronized boolean reserve(String name, int qty){
        int have = stock.getOrDefault(name, 0);
        if (have >= qty){
            stock.put(name, have - qty);
            return true;
        }
        return false;
    }

    public synchronized void release(String name, int qty){
        stock.put(name, stock.getOrDefault(name, 0) + qty);
    }

    public Map<String,Integer> snapshot(){ return stock; }
}
