package com.example.demo.controller;

import com.example.demo.dto.request.ClassRequest;
import com.example.demo.entity.SchoolClass;
import com.example.demo.service.SchoolClassService;
import com.example.demo.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/classes")
@RequiredArgsConstructor
public class ClassController {

    private final SchoolClassService schoolClassService;
    private final StudentService studentService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("classes", schoolClassService.getAllClasses());
        return "schoolclass/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        SchoolClass sc = schoolClassService.getClassById(id);
        model.addAttribute("schoolClass", sc);
        model.addAttribute("students", studentService.getStudentsByClass(id));
        return "schoolclass/detail";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String createForm(Model model) {
        model.addAttribute("classRequest", new ClassRequest());
        model.addAttribute("isEdit", false);
        return "schoolclass/form";
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String create(@Valid @ModelAttribute ClassRequest req,
                         BindingResult result,
                         Model model,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "schoolclass/form";
        }
        try {
            SchoolClass sc = new SchoolClass();
            mapToEntity(req, sc);
            schoolClassService.saveClass(sc);
            ra.addFlashAttribute("successMsg", "Thêm lớp học thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/classes";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        SchoolClass sc = schoolClassService.getClassById(id);
        ClassRequest req = new ClassRequest(sc.getClassName(), sc.getDepartment(),
                sc.getMajor(), sc.getAcademicYear());
        model.addAttribute("classRequest", req);
        model.addAttribute("classId", id);
        model.addAttribute("isEdit", true);
        return "schoolclass/form";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute ClassRequest req,
                         BindingResult result,
                         Model model,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("classId", id);
            model.addAttribute("isEdit", true);
            return "schoolclass/form";
        }
        try {
            SchoolClass sc = schoolClassService.getClassById(id);
            mapToEntity(req, sc);
            schoolClassService.saveClass(sc);
            ra.addFlashAttribute("successMsg", "Cập nhật lớp học thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/classes";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            schoolClassService.deleteClass(id);
            ra.addFlashAttribute("successMsg", "Xóa lớp học thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/classes";
    }

    private void mapToEntity(ClassRequest req, SchoolClass sc) {
        sc.setClassName(req.getClassName());
        sc.setDepartment(req.getDepartment());
        sc.setMajor(req.getMajor());
        sc.setAcademicYear(req.getAcademicYear());
    }
}