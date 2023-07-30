package com.example.demo.repository;

import com.example.demo.model.CartItem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {
    Boolean existsByUserIdAndBookId(Long userId, Long bookId);

    Boolean existsByBookIdAndIsPaperbackAndUserId(Long bookId, Boolean isPaperback, Long idAuthenticated);

    CartItem findOneById(Long id);

    Boolean existsByIdAndIsDeleted(Long id, Boolean isDeleted);
}
