package com.example.eduwithbe.Study.Repository;

import com.example.eduwithbe.Study.Domain.StudyScrapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyScrapRepository extends JpaRepository<StudyScrapEntity, Long> {
    // 스터디 스크랩 정보 조회
    @Query("select sc.s_no from StudyScrapEntity sc where sc.email = :email")
    List<Long> findMyStudyScrapInfo(String email);

    // 취소할 스크랩 정보 조회
    @Query("select sc from StudyScrapEntity sc where sc.email = :email and sc.s_no = :s_no")
    Optional<StudyScrapEntity> findStudyScrapEntityByEmailAndAndS_no(@Param("email") String email, @Param("s_no") Long s_no);
}
