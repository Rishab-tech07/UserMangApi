package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "users")
public class User {
//    @Version
//    private Integer version;

    public User(){
        // Default constructor for JPA

    }
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
}
