package com.example.eduwithbe.Study.Repository;

import com.example.eduwithbe.Study.Domain.StudyRecruitmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StudyRepository extends JpaRepository<StudyRecruitmentEntity, Long> {
    List<StudyRecruitmentEntity> findByTagContaining(String keyword);

    // email이 작성한 모집글 조회
    @Query(value = "SELECT s FROM StudyRecruitmentEntity s where s.user.email = :email")
    List<StudyRecruitmentEntity> findByUserEmail(@Param("email") String email);


    @Query(value = "select s.s_no from StudyRecruitmentEntity s where s.user.email = :email")
    List<Long> findStdNumsByMyEmail(@Param("email") String myEmail);

    // 스터디 마감(반환값은 update한 레코드 수)
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE StudyRecruitmentEntity s SET s.recruitYN=:recruitYN where s.s_no=:s_no")
    int updateRecruitYN(@Param("s_no") Long s_no, @Param("recruitYN") char recruitYN);
}
