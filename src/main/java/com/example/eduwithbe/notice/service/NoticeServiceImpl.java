package com.example.eduwithbe.notice.service;

import com.example.eduwithbe.mentoring.domain.MentoringRecruitmentEntity;
import com.example.eduwithbe.mentoring.dto.MentoringRecruitListDto;
import com.example.eduwithbe.mentoring.dto.MentoringRecruitSaveDto;
import com.example.eduwithbe.mentoring.dto.MentoringRecruitSaveUserDto;
import com.example.eduwithbe.mentoring.repository.MentoringRecruitmentRepository;
import com.example.eduwithbe.notice.domain.NoticeEntity;
import com.example.eduwithbe.notice.dto.NoticeGetDto;
import com.example.eduwithbe.notice.dto.NoticeSaveDto;
import com.example.eduwithbe.notice.repository.NoticeRepository;
import com.example.eduwithbe.user.domain.UserEntity;
import com.example.eduwithbe.user.dto.UserUpdateDto;
import com.example.eduwithbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private final NoticeRepository noticeRepository;

    @Autowired
    private final UserRepository userRepository;

    //알림 저장
    public NoticeEntity saveNotice(NoticeSaveDto dto) {
        NoticeSaveDto noticeSaveDto = new NoticeSaveDto();
        noticeSaveDto.setUser(dto.getUser());
        noticeSaveDto.setTitle(dto.getTitle());
        noticeSaveDto.setDate(CurrentDateTime());
        noticeSaveDto.setField(dto.getField());
        noticeSaveDto.setRead(dto.getRead());

        NoticeEntity noticeEntity = noticeSaveDto.toEntity();
        noticeRepository.save(noticeEntity);
        return noticeEntity;
    }

    //알림 리스트 조회
    public List<NoticeGetDto> findByEmailNotice(String email){
        List<NoticeEntity> notice = noticeRepository.findByEmailNotice(email);
        return notice.stream()
                .map(NoticeGetDto::new)
                .collect(Collectors.toList());
    }

    //알림 읽음 표시
    public String updateNotice(String email, Long notice_no) {
        NoticeEntity noticeEntity = noticeRepository.findById(notice_no).orElseThrow(() -> new IllegalArgumentException("해당 멘토링이 존재하지 않습니다." + notice_no));
        if(!Objects.equals(email, noticeEntity.getUser().getEmail())) return "NO";
        else {
            noticeRepository.updateNoticeByEmail(notice_no);
            return "OK";
        }
    }

    //알림 하나 삭제
    public String deleteNotice(NoticeEntity noticeEntity){
        noticeRepository.delete(noticeEntity);
        return "OK";
    }

    //알림 하나 조회
    public NoticeEntity findByNoticeId(Long notice_no){
        return noticeRepository.findById(notice_no).orElseThrow();
    }

    public Date CurrentDateTime() { //현재 날짜 구하기
        LocalDate now = LocalDate.now();
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        // return now.format(formatter);
        return Date.valueOf(LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth()));
    }
}
