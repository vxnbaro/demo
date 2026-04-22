package com.example.demo.controller;

import com.example.demo.dto.request.SubjectRequest;
import com.example.demo.entity.Subject;
import com.example.demo.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "subject/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String createForm(Model model) {
        model.addAttribute("subjectRequest", new SubjectRequest());
        model.addAttribute("isEdit", false);
        return "subject/form";
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String create(@Valid @ModelAttribute SubjectRequest req,
                         BindingResult result,
                         Model model,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "subject/form";
        }
        try {
            Subject subject = new Subject();
            mapToEntity(req, subject);
            subjectService.saveSubject(subject);
            ra.addFlashAttribute("successMsg", "Thêm môn học thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/subjects";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        Subject subject = subjectService.getSubjectById(id);
        SubjectRequest req = new SubjectRequest(
                subject.getSubjectCode(), subject.getSubjectName(),
                subject.getCredits(), subject.getDescription());
        model.addAttribute("subjectRequest", req);
        model.addAttribute("subjectId", id);
        model.addAttribute("isEdit", true);
        return "subject/form";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute SubjectRequest req,
                         BindingResult result,
                         Model model,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("subjectId", id);
            model.addAttribute("isEdit", true);
            return "subject/form";
        }
        try {
            Subject subject = subjectService.getSubjectById(id);
            mapToEntity(req, subject);
            subjectService.saveSubject(subject);
            ra.addFlashAttribute("successMsg", "Cập nhật môn học thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/subjects";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            subjectService.deleteSubject(id);
            ra.addFlashAttribute("successMsg", "Xóa môn học thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/subjects";
    }

    private void mapToEntity(SubjectRequest req, Subject subject) {
        subject.setSubjectCode(req.getSubjectCode());
        subject.setSubjectName(req.getSubjectName());
        subject.setCredits(req.getCredits());
        subject.setDescription(req.getDescription());
    }
}