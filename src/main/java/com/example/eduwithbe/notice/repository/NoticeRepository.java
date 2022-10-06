package com.example.eduwithbe.notice.repository;

import com.example.eduwithbe.notice.domain.NoticeEntity;
import com.example.eduwithbe.user.domain.UserAttendanceEntity;
import com.example.eduwithbe.user.domain.UserEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
    @Query("select m from NoticeEntity m where m.user.email = :email")
    List<NoticeEntity> findByEmail(@Param("email") String email, Sort sort);

    @Transactional
    @Modifying
    @Query("update NoticeEntity u set u.check_read = 'Y' where u.notice_no = :notice_no")
    void updateNoticeByEmail(Long notice_no);
}
