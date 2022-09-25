package com.example.eduwithbe.notice.service;

import com.example.eduwithbe.notice.domain.NoticeEntity;
import com.example.eduwithbe.notice.dto.NoticeGetDto;
import com.example.eduwithbe.notice.dto.NoticeSaveDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoticeService {

    NoticeEntity saveNotice(NoticeSaveDto dto);

    List<NoticeGetDto> findByEmailNotice(String email);

    String updateNotice(String email, Long notice_no);

    String deleteNotice(NoticeEntity noticeEntity);

    NoticeEntity findByNoticeId(Long notice_no);
}
