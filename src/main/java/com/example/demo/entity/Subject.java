package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subjects")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Mã môn học không được để trống")
    @Column(name = "subject_code", unique = true, nullable = false, length = 20)
    private String subjectCode;

    @NotBlank(message = "Tên môn học không được để trống")
    @Column(name = "subject_name", nullable = false, length = 100)
    private String subjectName;

    @Min(value = 1, message = "Số tín chỉ phải >= 1")
    @Max(value = 10, message = "Số tín chỉ phải <= 10")
    @Column(nullable = false)
    private int credits;

    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Grade> grades = new ArrayList<>();
}