package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "users")
public class User {
    @Version
    private Integer version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
}
