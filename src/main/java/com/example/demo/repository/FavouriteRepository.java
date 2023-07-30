package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Favourite;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    Boolean existsByUserIdAndBookId(Long userId, Long bookId);

    Boolean existsByBookIdAndUserId(Long bookId, Long idAuthenticated);

    Favourite findOneById(Long id);
}
