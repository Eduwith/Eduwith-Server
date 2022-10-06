package com.example.eduwithbe.Study.Service;

import com.example.eduwithbe.Study.Domain.StudyApplyEntity;
import com.example.eduwithbe.Study.Domain.StudyRecruitmentEntity;
import com.example.eduwithbe.Study.Domain.StudyingEntity;
import com.example.eduwithbe.Study.Dto.StudyRecruitDto;
import com.example.eduwithbe.Study.Dto.UserDetailDto;
import com.example.eduwithbe.Study.Repository.StudyApplyRepository;
import com.example.eduwithbe.Study.Repository.StudyRepository;
import com.example.eduwithbe.Study.Repository.StudyingRepository;
import com.example.eduwithbe.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyAdminServiceImpl implements StudyAdminService {
    private final StudyRepository studyRepository;
    private final StudyApplyRepository studyApplyRepository;
    private final StudyingRepository studyingRepository;


    // 내가 모집한 스터디 확인
    @Override
    public List<StudyRecruitDto> findMyStudyRecruits(String myEmail) {
        List<StudyRecruitmentEntity> studyRecruitEntities = studyRepository.findAllByEmail(myEmail);
        return studyRecruitEntities.stream()
                .map(StudyRecruitDto::new)
                .collect(Collectors.toList());
    }


    // 스터디 마감하기
    @Override
    public String finishStudy(Long stdNo) {
        studyRepository.updateRecruitYN(stdNo);
        return "success";
    }

    // 스터디 신청자 정보 확인
    public UserDetailDto findUserDetail(Long applyNo) {
        // 신청자 추출
        UserEntity applicant = studyApplyRepository.findById(applyNo)
                .orElseThrow(() -> new IllegalArgumentException("findUserDetail : 해당 지원 정보가 없습니다."))
                .getUser();

        return UserDetailDto.builder()
                .profile_img("")
                .email(applicant.getEmail())
                .name(applicant.getName())
                .age(applicant.getAge())
                .build();

    }

    // 신청자 수락하기
    @Override
    public String saveStudying(Long stdNo, Long applyNo) {
        StudyApplyEntity studyApply = studyApplyRepository.findById(applyNo)
                .orElseThrow(() -> new IllegalArgumentException("saveStudying : 지원 정보가 없습니다."));
        StudyRecruitmentEntity studyRecruitment = studyRepository.findById(stdNo)
                .orElseThrow(() -> new IllegalArgumentException("saveStudying : 해당 스터디가 없습니다."));

        // StudyApply의 result를 'Y'로 수정
        studyApplyRepository.updateApplyResult(applyNo, 'Y');

        // 스터디 모집글&신청자 이메일 정보 저장
        studyingRepository.save(StudyingEntity.builder()
                .studyRecruitment(studyRecruitment)
                .email(studyApply.getUser().getEmail())
                .build());

        return "success";
    }

    // 신청자 거절하기
    @Override
    public String deleteStudyApply(Long applyNo) {
        StudyApplyEntity studyApply = studyApplyRepository.findById(applyNo)
                .orElseThrow(() -> new IllegalArgumentException("deleteStudyApply : 지원 정보가 없습니다."));

        studyApplyRepository.delete(studyApply);
        return "success";
    }

}