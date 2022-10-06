package com.example.eduwithbe.Study.Service;

import com.example.eduwithbe.Study.Dto.StudyRecruitDto;
import com.example.eduwithbe.Study.Dto.UserDetailDto;

import java.util.List;

public interface StudyAdminService {
    List<StudyRecruitDto> findMyStudyRecruits(String myEmail);    // 내가 모집한 스터디 확인

    String finishStudy(final Long stdNo);   // 스터디 모집 마감하기

    UserDetailDto findUserDetail(Long applyNo); // 스터디 신청자 정보 확인

    String saveStudying(Long stdNo, Long applyNo); // 신청자 수락하기

    String deleteStudyApply(Long applyNo); // 신청자 거절하기
}
