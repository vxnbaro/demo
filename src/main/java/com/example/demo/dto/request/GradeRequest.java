package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class GradeRequest {

    @NotNull(message = "Vui lòng chọn sinh viên")
    private Long studentId;

    @NotNull(message = "Vui lòng chọn môn học")
    private Long subjectId;

    @NotBlank(message = "Học kỳ không được để trống")
    private String semester;

    @DecimalMin(value = "0.0", message = "Điểm phải >= 0")
    @DecimalMax(value = "10.0", message = "Điểm phải <= 10")
    private Double attendanceScore;

    @DecimalMin(value = "0.0", message = "Điểm phải >= 0")
    @DecimalMax(value = "10.0", message = "Điểm phải <= 10")
    private Double midtermScore;

    @NotNull(message = "Điểm cuối kỳ không được để trống")
    @DecimalMin(value = "0.0", message = "Điểm phải >= 0")
    @DecimalMax(value = "10.0", message = "Điểm phải <= 10")
    private Double finalScore;

    private String notes;
}