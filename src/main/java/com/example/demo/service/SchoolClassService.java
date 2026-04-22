package com.example.demo.service;

import com.example.demo.entity.SchoolClass;
import com.example.demo.repository.SchoolClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;

    public List<SchoolClass> getAllClasses() {
        return schoolClassRepository.findAll();
    }

    public SchoolClass getClassById(Long id) {
        return schoolClassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học với ID: " + id));
    }

    public SchoolClass saveClass(SchoolClass schoolClass) {
        if (schoolClass.getId() == null && schoolClassRepository.existsByClassName(schoolClass.getClassName())) {
            throw new RuntimeException("Tên lớp đã tồn tại: " + schoolClass.getClassName());
        }
        return schoolClassRepository.save(schoolClass);
    }

    public void deleteClass(Long id) {
        schoolClassRepository.deleteById(id);
    }

    public long getTotalClasses() {
        return schoolClassRepository.count();
    }
}