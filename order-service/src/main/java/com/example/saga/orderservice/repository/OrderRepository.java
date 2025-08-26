package com.example.saga.orderservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.saga.orderservice.entity.Orders;

public interface OrderRepository extends JpaRepository<Orders, String> {}

