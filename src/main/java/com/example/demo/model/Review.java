package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Long userId;

    private Integer rating;

    private Integer upvotes;

    @Column(name = "review_content")
    private String reviewContent;

    public Review() {

    }

    public Review(Long userId, Integer rating, Integer upvotes) {
        this.userId = userId;
        this.rating = rating;
        this.upvotes = upvotes;
    }

    // getters and setters
    public Integer getUpvotes() {
        return this.upvotes;
    }

    public void setUpvotes(Integer upvotes) {
        this.upvotes = upvotes;
    }

    public Long getUserId() {
        return this.userId;
    }
}
