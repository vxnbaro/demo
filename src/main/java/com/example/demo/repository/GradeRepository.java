package com.example.demo.repository;

import com.example.demo.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    List<Grade> findByStudentId(Long studentId);

    List<Grade> findByStudentIdAndSemester(Long studentId, String semester);

    List<Grade> findBySubjectIdAndSemester(Long subjectId, String semester);

    Optional<Grade> findByStudentIdAndSubjectIdAndSemester(Long studentId, Long subjectId, String semester);

    boolean existsByStudentIdAndSubjectIdAndSemester(Long studentId, Long subjectId, String semester);

    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.subject.id = :subjectId AND g.semester = :semester")
    Double findAverageScoreBySubjectAndSemester(@Param("subjectId") Long subjectId, @Param("semester") String semester);

    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.student.id = :studentId")
    Double findGpaByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(g) FROM Grade g WHERE g.student.id = :studentId AND g.score >= 5.0")
    long countPassedByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(g) FROM Grade g WHERE g.student.id = :studentId AND g.score < 5.0")
    long countFailedByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT DISTINCT g.semester FROM Grade g ORDER BY g.semester DESC")
    List<String> findAllSemesters();
}