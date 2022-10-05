package com.example.eduwithbe.mentoring.repository;

import com.example.eduwithbe.mentoring.domain.MentoringEntity;
import com.example.eduwithbe.mentoring.domain.MentoringReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MentoringReviewRepository  extends JpaRepository<MentoringReviewEntity, Long> {

    @Query("select m from MentoringReviewEntity m where m.writer.email = :email")
    List<MentoringReviewEntity> findByStar(@Param("email") String email);
}
