package com.example.demo.service;

import com.example.demo.entity.SchoolClass;
import com.example.demo.entity.Student;
import com.example.demo.repository.SchoolClassRepository;
import com.example.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;

    public Page<Student> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    public Page<Student> searchStudents(String keyword, Pageable pageable) {
        return studentRepository.searchStudents(keyword, pageable);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với ID: " + id));
    }

    public Student getStudentByCode(String code) {
        return studentRepository.findByStudentCode(code)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với mã: " + code));
    }

    public Student saveStudent(Student student) {
        if (student.getId() == null && studentRepository.existsByStudentCode(student.getStudentCode())) {
            throw new RuntimeException("Mã sinh viên đã tồn tại: " + student.getStudentCode());
        }
        if (student.getId() == null && studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng: " + student.getEmail());
        }
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }

    public List<Student> getStudentsByClass(Long classId) {
        return studentRepository.findBySchoolClassId(classId);
    }

    public long getTotalStudents() {
        return studentRepository.count();
    }

    public List<SchoolClass> getAllClasses() {
        return schoolClassRepository.findAll();
    }
}