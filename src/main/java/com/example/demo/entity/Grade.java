package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "grades",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "subject_id", "semester"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @NotNull(message = "Điểm không được để trống")
    @DecimalMin(value = "0.0", message = "Điểm phải >= 0")
    @DecimalMax(value = "10.0", message = "Điểm phải <= 10")
    @Column(nullable = false)
    private Double score;

    // Điểm thành phần
    @Column(name = "midterm_score")
    private Double midtermScore;

    @Column(name = "final_score")
    private Double finalScore;

    @Column(name = "attendance_score")
    private Double attendanceScore;

    @NotBlank(message = "Học kỳ không được để trống")
    @Column(nullable = false, length = 20)
    private String semester; // e.g. "2024-1", "2024-2"

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(length = 200)
    private String notes;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculateScore();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateScore();
    }

    // Tự tính điểm tổng kết nếu có điểm thành phần
    private void calculateScore() {
        if (midtermScore != null && finalScore != null) {
            double attendance = attendanceScore != null ? attendanceScore : 10.0;
            score = attendance * 0.1 + midtermScore * 0.3 + finalScore * 0.6;
            score = Math.round(score * 10.0) / 10.0;
        }
    }

    // Chữ điểm
    public String getLetterGrade() {
        if (score == null) return "N/A";
        if (score >= 9.0) return "A+";
        if (score >= 8.5) return "A";
        if (score >= 8.0) return "B+";
        if (score >= 7.0) return "B";
        if (score >= 6.5) return "C+";
        if (score >= 5.5) return "C";
        if (score >= 5.0) return "D+";
        if (score >= 4.0) return "D";
        return "F";
    }

    public boolean isPassed() {
        return score != null && score >= 5.0;
    }
}