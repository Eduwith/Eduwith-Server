package com.example.eduwithbe.mentoring.repository;

import com.example.eduwithbe.mentoring.domain.MentoringApplyEntity;
import com.example.eduwithbe.mentoring.domain.MentoringRecruitmentEntity;
import com.example.eduwithbe.mentoring.domain.MentoringScrapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentoringRecruitScrapRepository extends JpaRepository<MentoringScrapEntity, Long> {

    @Query("select m from MentoringScrapEntity m where m.user.email = :email and m.m_no.m_no = :m_no")
    MentoringScrapEntity findByEmailAndMNoMentoringScrap(@Param("email") String email, @Param("m_no") Long m_no);

    @Query("select m from MentoringScrapEntity m where m.user.email = :email and m.m_no.m_no = :m_no")
    List<MentoringScrapEntity> findByEmailAndMNoMentoringScrapList(@Param("email") String email, @Param("m_no") Long m_no);

    @Query("select m from MentoringScrapEntity m where m.user.email = :email")
    List<MentoringScrapEntity> findByEmailMentoringScrap(@Param("email") String email);


}
