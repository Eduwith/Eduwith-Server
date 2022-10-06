package com.example.eduwithbe.mentoring.repository;

import com.example.eduwithbe.mentoring.domain.MentoringRecruitmentEntity;
import com.example.eduwithbe.mentoring.dto.MentoringRecruitUpdateDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MentoringRecruitmentRepository extends JpaRepository<MentoringRecruitmentEntity, Long> {
    List<MentoringRecruitmentEntity> findByTitleContaining(String keyword);

    @Query("select p from MentoringRecruitmentEntity p where p.field In(:field) and p.region In(:region) and p.m_period In(:m_period) and p.way In(:way)")
    List<MentoringRecruitmentEntity> findByFilter(@Param("field") List<String> field, @Param("region") List<String> region, @Param("m_period") List<Integer> m_period, @Param("way") List<String> way);

    @Query("select m from MentoringRecruitmentEntity m where m.user.email = :email and m.role = 'O'")
    List<MentoringRecruitmentEntity> findByEmailMentoringMentor(@Param("email") String email);

    @Query("select m from MentoringRecruitmentEntity m where m.user.email = :email and m.role = 'E'")
    List<MentoringRecruitmentEntity> findByEmailMentoringMentee(@Param("email") String email);

    @Query("select m from MentoringRecruitmentEntity m where m.role = 'O'")
    List<MentoringRecruitmentEntity> findByMentoringMentor();

    @Query("select m from MentoringRecruitmentEntity m where m.role = 'E'")
    List<MentoringRecruitmentEntity> findByMentoringMentee();

    @Query("select m from MentoringRecruitmentEntity m where m.region = :region and m.user.email not in :email")
    List<MentoringRecruitmentEntity> findByDistance(String email, String region);

    @Query("select m from MentoringRecruitmentEntity m where m.region like %?2% and m.region not in(?3) and m.user.email not in ?1")
    List<MentoringRecruitmentEntity> findByDistanceWide(String email, String region, String region2);

    @Transactional
    @Modifying
    @Query("update MentoringRecruitmentEntity m set m.title = :title, m.role = :role, m.field = :field, m.region = :region, m.m_period = :m_period, m.way = :way, m.keyword = :keyword, m.info = :info where m.m_no = :m_no")
    void updateByMentoringRecruit(Long m_no, String title, String role, String field, String region, int m_period, String way, String keyword, String info);

    //select * from (select * from mentoring_recruitment m where m.email = '123@gmail.com' and m.role = 'E') AS r left join  mentoring_apply a on a.m_no = r.m_no ;
}


