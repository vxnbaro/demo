package com.example.demo.controller;

import com.example.demo.dto.request.StudentRequest;
import com.example.demo.entity.SchoolClass;
import com.example.demo.entity.Student;
import com.example.demo.service.GradeService;
import com.example.demo.service.SchoolClassService;
import com.example.demo.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final SchoolClassService schoolClassService;
    private final GradeService gradeService;

    // ── Danh sách sinh viên ──────────────────────────────────────
    @GetMapping
    public String list(@RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("studentCode").ascending());
        Page<Student> studentPage = keyword.isBlank()
                ? studentService.getAllStudents(pageable)
                : studentService.searchStudents(keyword, pageable);

        model.addAttribute("studentPage", studentPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        return "student/list";
    }

    // ── Chi tiết sinh viên ───────────────────────────────────────
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        model.addAttribute("grades", gradeService.getGradesByStudent(id));
        model.addAttribute("gpa", gradeService.getGpaByStudent(id));
        model.addAttribute("passed", gradeService.countPassedByStudent(id));
        model.addAttribute("failed", gradeService.countFailedByStudent(id));
        model.addAttribute("semesters", gradeService.getAllSemesters());
        return "student/detail";
    }

    // ── Form tạo mới ─────────────────────────────────────────────
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("studentRequest", new StudentRequest());
        model.addAttribute("classes", schoolClassService.getAllClasses());
        model.addAttribute("isEdit", false);
        return "student/form";
    }

    // ── Lưu mới ─────────────────────────────────────────────────
    @PostMapping("/new")
    public String create(@Valid @ModelAttribute StudentRequest req,
                         BindingResult result,
                         Model model,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("classes", schoolClassService.getAllClasses());
            model.addAttribute("isEdit", false);
            return "student/form";
        }
        try {
            Student student = mapToEntity(req, new Student());
            studentService.saveStudent(student);
            ra.addFlashAttribute("successMsg", "Thêm sinh viên thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/students";
    }

    // ── Form chỉnh sửa ───────────────────────────────────────────
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        StudentRequest req = mapToRequest(student);
        model.addAttribute("studentRequest", req);
        model.addAttribute("studentId", id);
        model.addAttribute("classes", schoolClassService.getAllClasses());
        model.addAttribute("isEdit", true);
        return "student/form";
    }

    // ── Cập nhật ─────────────────────────────────────────────────
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute StudentRequest req,
                         BindingResult result,
                         Model model,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("studentId", id);
            model.addAttribute("classes", schoolClassService.getAllClasses());
            model.addAttribute("isEdit", true);
            return "student/form";
        }
        try {
            Student student = studentService.getStudentById(id);
            mapToEntity(req, student);
            studentService.saveStudent(student);
            ra.addFlashAttribute("successMsg", "Cập nhật sinh viên thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/students";
    }

    // ── Xóa ─────────────────────────────────────────────────────
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            studentService.deleteStudent(id);
            ra.addFlashAttribute("successMsg", "Xóa sinh viên thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/students";
    }

    // ── Helpers ──────────────────────────────────────────────────
    private Student mapToEntity(StudentRequest req, Student student) {
        student.setStudentCode(req.getStudentCode());
        student.setFullName(req.getFullName());
        student.setEmail(req.getEmail());
        student.setDateOfBirth(req.getDateOfBirth());
        student.setGender(req.getGender());
        student.setAddress(req.getAddress());
        student.setPhoneNumber(req.getPhoneNumber());
        if (req.getClassId() != null) {
            SchoolClass sc = new SchoolClass();
            sc.setId(req.getClassId());
            student.setSchoolClass(sc);
        }
        return student;
    }

    private StudentRequest mapToRequest(Student student) {
        StudentRequest req = new StudentRequest();
        req.setStudentCode(student.getStudentCode());
        req.setFullName(student.getFullName());
        req.setEmail(student.getEmail());
        req.setDateOfBirth(student.getDateOfBirth());
        req.setGender(student.getGender());
        req.setAddress(student.getAddress());
        req.setPhoneNumber(student.getPhoneNumber());
        if (student.getSchoolClass() != null) {
            req.setClassId(student.getSchoolClass().getId());
        }
        return req;
    }
}