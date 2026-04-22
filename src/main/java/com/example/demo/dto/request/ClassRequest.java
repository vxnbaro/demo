package com.example.demo.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ClassRequest {

    @NotBlank(message = "Tên lớp không được để trống")
    @Size(max = 50, message = "Tên lớp tối đa 50 ký tự")
    private String className;

    @Size(max = 100)
    private String department;

    @Size(max = 100)
    private String major;

    @Size(max = 20)
    private String academicYear;
}