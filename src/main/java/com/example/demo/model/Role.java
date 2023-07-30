package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public Role() {

    }

    public Role(ERole name) {
        this.name = name;
    }

    // getters and setters
    public Integer getid() {
        return id;
    }

    public void setId() {
        return;
    }

    public ERole getName() {
        return name;
    }

    public void setName() {
        return;
    }
}