package com.example.demo.service;

import com.example.demo.entity.Grade;
import com.example.demo.entity.Student;
import com.example.demo.entity.Subject;
import com.example.demo.repository.GradeRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public List<Grade> getGradesByStudent(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    public List<Grade> getGradesByStudentAndSemester(Long studentId, String semester) {
        return gradeRepository.findByStudentIdAndSemester(studentId, semester);
    }

    public List<Grade> getGradesBySubjectAndSemester(Long subjectId, String semester) {
        return gradeRepository.findBySubjectIdAndSemester(subjectId, semester);
    }

    public Grade getGradeById(Long id) {
        return gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy điểm với ID: " + id));
    }

    public Grade saveGrade(Grade grade) {
        boolean exists = gradeRepository.existsByStudentIdAndSubjectIdAndSemester(
                grade.getStudent().getId(),
                grade.getSubject().getId(),
                grade.getSemester()
        );
        if (exists && grade.getId() == null) {
            throw new RuntimeException("Sinh viên đã có điểm môn học này trong học kỳ này");
        }
        return gradeRepository.save(grade);
    }

    public Grade saveGradeForStudentSubject(Long studentId, Long subjectId, String semester,
                                            Double score, Double midterm, Double finalScore, Double attendance, String notes) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy môn học"));

        Grade grade = gradeRepository.findByStudentIdAndSubjectIdAndSemester(studentId, subjectId, semester)
                .orElse(new Grade());

        grade.setStudent(student);
        grade.setSubject(subject);
        grade.setSemester(semester);
        grade.setScore(score);
        grade.setMidtermScore(midterm);
        grade.setFinalScore(finalScore);
        grade.setAttendanceScore(attendance);
        grade.setNotes(notes);

        return gradeRepository.save(grade);
    }

    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }

    public Double getGpaByStudent(Long studentId) {
        Double gpa = gradeRepository.findGpaByStudentId(studentId);
        return gpa != null ? Math.round(gpa * 100.0) / 100.0 : 0.0;
    }

    public Double getAverageScoreBySubjectAndSemester(Long subjectId, String semester) {
        Double avg = gradeRepository.findAverageScoreBySubjectAndSemester(subjectId, semester);
        return avg != null ? Math.round(avg * 100.0) / 100.0 : 0.0;
    }

    public List<String> getAllSemesters() {
        return gradeRepository.findAllSemesters();
    }

    public long countPassedByStudent(Long studentId) {
        return gradeRepository.countPassedByStudentId(studentId);
    }

    public long countFailedByStudent(Long studentId) {
        return gradeRepository.countFailedByStudentId(studentId);
    }

    public long getTotalGrades() {
        return gradeRepository.count();
    }
}