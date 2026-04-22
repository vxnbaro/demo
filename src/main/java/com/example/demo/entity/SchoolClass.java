package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "school_classes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SchoolClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên lớp không được để trống")
    @Column(name = "class_name", unique = true, nullable = false, length = 50)
    private String className;

    @Column(length = 100)
    private String department; // Khoa

    @Column(length = 100)
    private String major; // Chuyên ngành

    @Column(name = "academic_year", length = 20)
    private String academicYear; // Khóa học, e.g. "2021-2025"

    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Student> students = new ArrayList<>();

    public int getStudentCount() {
        return students.size();
    }
}