package com.example.demo.repository;

import java.util.List;

import com.example.demo.model.Book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitle(String title);

    List<Book> findByUserId(Long author);

    Book findOneById(Long id);

}
