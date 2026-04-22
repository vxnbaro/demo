package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String role; // ROLE_ADMIN, ROLE_TEACHER, ROLE_STUDENT

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(length = 100)
    private String email;
}