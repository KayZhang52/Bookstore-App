package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "is_paperback")
    private Boolean isPaperback;

    private String status;

    @Column(name = "create_date", columnDefinition = "DATE")
    private LocalDate createDate;

    @Column(name = "create_time", columnDefinition = "TIME")
    private LocalTime createTime;

    public Order() {

    }

    public Order(Long bookId, Long userId, Boolean isPaperback, String status, LocalDate date, LocalTime time) {
        this.userId = userId;
        this.bookId = bookId;
        this.isPaperback = isPaperback;
        this.status = status;
        this.createDate = date;
        this.createTime = time;
    }

    // getters and setters
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUserId() {
        return this.userId;
    }
}
