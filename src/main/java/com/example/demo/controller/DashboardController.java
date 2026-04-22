package com.example.demo.controller;

import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final StudentService studentService;
    private final SubjectService subjectService;
    private final GradeService gradeService;
    private final SchoolClassService schoolClassService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("totalStudents", studentService.getTotalStudents());
        model.addAttribute("totalSubjects", subjectService.getTotalSubjects());
        model.addAttribute("totalClasses", schoolClassService.getTotalClasses());
        model.addAttribute("totalGrades", gradeService.getTotalGrades());
        model.addAttribute("semesters", gradeService.getAllSemesters());
        model.addAttribute("classes", schoolClassService.getAllClasses());
        return "dashboard";
    }
}