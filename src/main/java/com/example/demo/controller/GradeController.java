package com.example.demo.controller;

import com.example.demo.dto.request.GradeRequest;
import com.example.demo.entity.Grade;
import com.example.demo.service.GradeService;
import com.example.demo.service.StudentService;
import com.example.demo.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;
    private final StudentService studentService;
    private final SubjectService subjectService;

    // ── Danh sách điểm theo môn + học kỳ ────────────────────────
    @GetMapping
    public String list(@RequestParam(required = false) Long subjectId,
                       @RequestParam(required = false) String semester,
                       Model model) {
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("semesters", gradeService.getAllSemesters());
        model.addAttribute("selectedSubjectId", subjectId);
        model.addAttribute("selectedSemester", semester);

        if (subjectId != null && semester != null && !semester.isBlank()) {
            model.addAttribute("grades", gradeService.getGradesBySubjectAndSemester(subjectId, semester));
            model.addAttribute("avgScore", gradeService.getAverageScoreBySubjectAndSemester(subjectId, semester));
        }
        return "grade/list";
    }

    // ── Form nhập điểm ───────────────────────────────────────────
    @GetMapping("/new")
    public String createForm(@RequestParam(required = false) Long studentId, Model model) {
        GradeRequest req = new GradeRequest();
        if (studentId != null) req.setStudentId(studentId);
        model.addAttribute("gradeRequest", req);
        model.addAttribute("students", studentService.getAllStudents(
                org.springframework.data.domain.PageRequest.of(0, 1000)).getContent());
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("semesters", gradeService.getAllSemesters());
        model.addAttribute("isEdit", false);
        return "grade/form";
    }

    // ── Lưu điểm mới ────────────────────────────────────────────
    @PostMapping("/new")
    public String create(@Valid @ModelAttribute GradeRequest req,
                         BindingResult result,
                         Model model,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("students", studentService.getAllStudents(
                    org.springframework.data.domain.PageRequest.of(0, 1000)).getContent());
            model.addAttribute("subjects", subjectService.getAllSubjects());
            model.addAttribute("semesters", gradeService.getAllSemesters());
            model.addAttribute("isEdit", false);
            return "grade/form";
        }
        try {
            gradeService.saveGradeForStudentSubject(
                    req.getStudentId(), req.getSubjectId(), req.getSemester(),
                    null, req.getMidtermScore(), req.getFinalScore(),
                    req.getAttendanceScore(), req.getNotes());
            ra.addFlashAttribute("successMsg", "Nhập điểm thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/grades";
    }

    // ── Form sửa điểm ───────────────────────────────────────────
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Grade grade = gradeService.getGradeById(id);
        GradeRequest req = new GradeRequest();
        req.setStudentId(grade.getStudent().getId());
        req.setSubjectId(grade.getSubject().getId());
        req.setSemester(grade.getSemester());
        req.setAttendanceScore(grade.getAttendanceScore());
        req.setMidtermScore(grade.getMidtermScore());
        req.setFinalScore(grade.getFinalScore());
        req.setNotes(grade.getNotes());

        model.addAttribute("gradeRequest", req);
        model.addAttribute("gradeId", id);
        model.addAttribute("grade", grade);
        model.addAttribute("students", studentService.getAllStudents(
                org.springframework.data.domain.PageRequest.of(0, 1000)).getContent());
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("semesters", gradeService.getAllSemesters());
        model.addAttribute("isEdit", true);
        return "grade/form";
    }

    // ── Cập nhật điểm ───────────────────────────────────────────
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute GradeRequest req,
                         BindingResult result,
                         Model model,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            Grade grade = gradeService.getGradeById(id);
            model.addAttribute("gradeId", id);
            model.addAttribute("grade", grade);
            model.addAttribute("students", studentService.getAllStudents(
                    org.springframework.data.domain.PageRequest.of(0, 1000)).getContent());
            model.addAttribute("subjects", subjectService.getAllSubjects());
            model.addAttribute("semesters", gradeService.getAllSemesters());
            model.addAttribute("isEdit", true);
            return "grade/form";
        }
        try {
            Grade grade = gradeService.getGradeById(id);
            grade.setAttendanceScore(req.getAttendanceScore());
            grade.setMidtermScore(req.getMidtermScore());
            grade.setFinalScore(req.getFinalScore());
            grade.setNotes(req.getNotes());
            gradeService.saveGrade(grade);
            ra.addFlashAttribute("successMsg", "Cập nhật điểm thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/grades";
    }

    // ── Xóa điểm ────────────────────────────────────────────────
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            gradeService.deleteGrade(id);
            ra.addFlashAttribute("successMsg", "Xóa điểm thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/grades";
    }
}