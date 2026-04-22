package com.example.demo.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SubjectRequest {

    @NotBlank(message = "Mã môn học không được để trống")
    @Size(max = 20, message = "Mã môn học tối đa 20 ký tự")
    private String subjectCode;

    @NotBlank(message = "Tên môn học không được để trống")
    @Size(max = 100, message = "Tên môn học tối đa 100 ký tự")
    private String subjectName;

    @Min(value = 1, message = "Số tín chỉ phải >= 1")
    @Max(value = 10, message = "Số tín chỉ phải <= 10")
    private int credits;

    @Size(max = 500)
    private String description;
}