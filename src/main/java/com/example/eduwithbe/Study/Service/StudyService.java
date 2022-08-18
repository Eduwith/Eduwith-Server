package com.example.eduwithbe.Study.Service;

import com.example.eduwithbe.Study.Dto.StudySaveRequestDto;
import com.example.eduwithbe.Study.Dto.StudyRecruitDto;
import com.example.eduwithbe.paging.CommonParams;

import java.util.List;
import java.util.Map;

public interface StudyService {
//    Map<String, Object> getAllStudies(CommonParams params);

    List<StudyRecruitDto> findAllStudies(); // 스터디 모집글 전체 조회

    StudyRecruitDto findStudyByNo(final Long stdNo); // 특정 스터디 모집글 조회

    List<StudyRecruitDto> findStudiesByTag(String keyword);     // 스터디 태그 검색

    String saveStudyApply(String email, Long s_no); // 스터디 지원하기

    Long registerStudy(final StudySaveRequestDto studyRequestDto, String email); // 스터디 모집글 등록

    String updateStudy(final Long stdNo, final StudySaveRequestDto studyReq); // 스터디 모집글 수정

    void deleteStudy(Long stdNo); // 스터디 모집글 삭제

    String saveStudyScrap(String myEmail, Long stdNo); // 스터디 스크랩 저장
}
