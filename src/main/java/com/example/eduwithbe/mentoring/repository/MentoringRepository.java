package com.example.eduwithbe.mentoring.repository;

import com.example.eduwithbe.mentoring.domain.MentoringEntity;
import com.example.eduwithbe.mentoring.domain.MentoringRecruitmentEntity;
import com.example.eduwithbe.mentoring.domain.MentoringScrapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MentoringRepository extends JpaRepository<MentoringEntity, Long> {
    @Query("select m from MentoringEntity m where (m.writer.email = :email or m.applicant.email = :email) and m.m_no.role = 'O'")
    List<MentoringEntity> findByApplicantOrWriterMentor(@Param("email") String email);

    @Query("select m from MentoringEntity m where (m.writer.email = :email or m.applicant.email = :email) and m.m_no.role = 'E'")
    List<MentoringEntity> findByApplicantOrWriterMentee(@Param("email") String email);

//    @Transactional
//    @Modifying
//    @Query("update MentoringEntity m set m.star = :star where m.mentoring_no = :mentoring_no")
//    void updateStarByMentoring(Long mentoring_no, Float star);
}
