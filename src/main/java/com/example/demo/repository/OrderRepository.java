package com.example.demo.repository;

import com.example.demo.model.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findOneById(Long id);

    Boolean existsByUserIdAndBookId(Long userId, Long bookId);
}
