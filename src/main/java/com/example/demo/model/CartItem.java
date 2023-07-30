package com.example.demo.model;

import java.sql.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "added_to_cart")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "is_paperback")
    private Boolean isPaperback;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public CartItem() {

    }

    public CartItem(Long userId, Long bookId, Boolean isPaperback) {
        this.userId = userId;
        this.bookId = bookId;
        this.isPaperback = isPaperback;
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getIsPaperBack() {
        return isPaperback;
    }

    public void setIsPaperback(Boolean isPaperback) {
        this.isPaperback = isPaperback;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
