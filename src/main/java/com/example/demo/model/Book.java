package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "has_paperback")
    private Boolean hasPaperback;

    private String status;

    public Book() {

    }

    public Book(String title, Long userId, Boolean hasPaperback, String status) {
        this.title = title;
        this.userId = userId;
        this.hasPaperback = hasPaperback;
        this.status = status;
    }

    // gets and sets
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getHasPaperback() {
        return hasPaperback;
    }

    public void setHasPaperback(Boolean hasPaperback) {
        this.hasPaperback = hasPaperback;
    }
}