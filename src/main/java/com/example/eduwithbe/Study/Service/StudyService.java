package com.example.eduwithbe.Study.Service;

import com.example.eduwithbe.Study.Dto.StudySaveRequestDto;
import com.example.eduwithbe.Study.Dto.StudyRecruitDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudyService {
    Page<StudyRecruitDto> studyPageList(Pageable pageable); // 스터디 목록 조회

    StudyRecruitDto findStudyByNo(final Long stdNo); // 특정 스터디 모집글 조회

    List<StudyRecruitDto> findStudiesByTag(String keyword);     // 스터디 태그 검색

    String saveStudyApply(String email, Long s_no); // 스터디 지원하기

    String registerStudy(StudySaveRequestDto studyRequestDto, String email); // 스터디 모집글 등록

    String updateStudy(final Long stdNo, final StudySaveRequestDto studyReq); // 스터디 모집글 수정

    void deleteStudy(Long stdNo); // 스터디 모집글 삭제

    String saveStudyScrap(String myEmail, Long stdNo); // 스터디 스크랩 저장

    String deleteStudyScrap(String email, Long stdNo); // 스터디 스크랩 취소

    List<Long> findStudyScrapInfo(String email); // 스터디 스크랩 정보 불러오기
}
