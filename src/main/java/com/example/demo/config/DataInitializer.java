package com.example.demo.config;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SchoolClassRepository classRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final GradeRepository gradeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return; // skip if data exists

        // Tạo tài khoản Admin
        userRepository.save(User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .role("ROLE_ADMIN")
                .fullName("Quản trị viên")
                .email("admin@school.edu.vn")
                .build());

        // Tạo tài khoản Giáo viên
        userRepository.save(User.builder()
                .username("teacher")
                .password(passwordEncoder.encode("teacher123"))
                .role("ROLE_TEACHER")
                .fullName("Nguyễn Văn Giáo")
                .email("teacher@school.edu.vn")
                .build());

        // Tạo lớp học
        SchoolClass class1 = classRepository.save(SchoolClass.builder()
                .className("CNTT-K21A")
                .department("Khoa Công nghệ thông tin")
                .major("Công nghệ phần mềm")
                .academicYear("2021-2025")
                .build());

        SchoolClass class2 = classRepository.save(SchoolClass.builder()
                .className("CNTT-K21B")
                .department("Khoa Công nghệ thông tin")
                .major("Hệ thống thông tin")
                .academicYear("2021-2025")
                .build());

        // Tạo sinh viên mẫu
        Student s1 = studentRepository.save(Student.builder()
                .studentCode("SV001")
                .fullName("Nguyễn Văn An")
                .email("an.nv@student.edu.vn")
                .dateOfBirth(LocalDate.of(2003, 1, 15))
                .gender("Nam")
                .phoneNumber("0901234567")
                .schoolClass(class1)
                .build());

        Student s2 = studentRepository.save(Student.builder()
                .studentCode("SV002")
                .fullName("Trần Thị Bình")
                .email("binh.tt@student.edu.vn")
                .dateOfBirth(LocalDate.of(2003, 5, 20))
                .gender("Nữ")
                .phoneNumber("0912345678")
                .schoolClass(class1)
                .build());

        Student s3 = studentRepository.save(Student.builder()
                .studentCode("SV003")
                .fullName("Lê Hoàng Cường")
                .email("cuong.lh@student.edu.vn")
                .dateOfBirth(LocalDate.of(2003, 8, 10))
                .gender("Nam")
                .phoneNumber("0923456789")
                .schoolClass(class2)
                .build());

        // Tạo môn học mẫu
        Subject math = subjectRepository.save(Subject.builder()
                .subjectCode("MATH101")
                .subjectName("Toán cao cấp")
                .credits(3)
                .description("Giải tích, đại số tuyến tính")
                .build());

        Subject oop = subjectRepository.save(Subject.builder()
                .subjectCode("OOP201")
                .subjectName("Lập trình hướng đối tượng")
                .credits(3)
                .description("Java OOP, Design Patterns")
                .build());

        Subject db = subjectRepository.save(Subject.builder()
                .subjectCode("DB301")
                .subjectName("Cơ sở dữ liệu")
                .credits(3)
                .description("SQL, ERD, Normalization")
                .build());

        // Tạo điểm mẫu
        String sem = "2024-1";
        gradeRepository.save(Grade.builder().student(s1).subject(math).semester(sem).score(8.5).midtermScore(8.0).finalScore(8.8).attendanceScore(9.0).build());
        gradeRepository.save(Grade.builder().student(s1).subject(oop).semester(sem).score(9.0).midtermScore(8.5).finalScore(9.2).attendanceScore(10.0).build());
        gradeRepository.save(Grade.builder().student(s1).subject(db).semester(sem).score(7.5).midtermScore(7.0).finalScore(7.8).attendanceScore(8.0).build());

        gradeRepository.save(Grade.builder().student(s2).subject(math).semester(sem).score(7.0).midtermScore(6.5).finalScore(7.2).attendanceScore(8.0).build());
        gradeRepository.save(Grade.builder().student(s2).subject(oop).semester(sem).score(8.0).midtermScore(7.5).finalScore(8.2).attendanceScore(9.0).build());
        gradeRepository.save(Grade.builder().student(s2).subject(db).semester(sem).score(9.5).midtermScore(9.0).finalScore(9.7).attendanceScore(10.0).build());

        gradeRepository.save(Grade.builder().student(s3).subject(math).semester(sem).score(6.0).midtermScore(5.5).finalScore(6.2).attendanceScore(7.0).build());
        gradeRepository.save(Grade.builder().student(s3).subject(oop).semester(sem).score(4.5).midtermScore(4.0).finalScore(4.8).attendanceScore(6.0).build());
        gradeRepository.save(Grade.builder().student(s3).subject(db).semester(sem).score(7.0).midtermScore(6.5).finalScore(7.2).attendanceScore(8.0).build());
    }
}