package com.example.eduwithbe.Study.Service;

import com.example.eduwithbe.Study.Domain.StudyApplyEntity;
import com.example.eduwithbe.Study.Domain.StudyRecruitmentEntity;
import com.example.eduwithbe.Study.Domain.StudyScrapEntity;
import com.example.eduwithbe.Study.Dto.StudySaveRequestDto;
import com.example.eduwithbe.Study.Dto.StudyRecruitDto;
import com.example.eduwithbe.Study.Repository.StudyApplyRepository;
import com.example.eduwithbe.Study.Repository.StudyRepository;
import com.example.eduwithbe.Study.Repository.StudyScrapRepository;
import com.example.eduwithbe.user.domain.UserEntity;
import com.example.eduwithbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService{

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final StudyApplyRepository studyApplyRepository;
    private final StudyScrapRepository studyScrapRepository;

    // 스터디 목록 조회
    @Override
    @Transactional
    public Page<StudyRecruitDto> studyPageList(Pageable pageable) {
        Page<StudyRecruitmentEntity> studyEntities = studyRepository.findAll(pageable);

        return studyEntities.map(StudyRecruitDto::new);
    }

    // 특정 모집글 상세정보 조회
    @Override
    public StudyRecruitDto findStudyByNo(final Long stdNo) {
        StudyRecruitmentEntity entity = studyRepository.findById(stdNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));
        return new StudyRecruitDto(entity);
    }

    // 스터디 키워드(태그) 검색
    @Override
    public List<StudyRecruitDto> findStudiesByTag(String keyword) {
        List<StudyRecruitmentEntity> studyRecruitmentEntities = studyRepository.findByTagContaining(keyword);
        return studyRecruitmentEntities.stream()
                .map(StudyRecruitDto::new)
                .collect(Collectors.toList());
    }

    // 스터디 신청하기
    @Override
    @Transactional
    public String saveStudyApply(String email, Long s_no) {
        // 신청자 정보, 신청한 스터디의 정보 가져오기
        UserEntity applicant = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("saveStudyApply : 사용자가 없습니다."));
        StudyRecruitmentEntity study = studyRepository.findById(s_no)
                .orElseThrow(() -> new IllegalArgumentException("saveStudyApply : 스터디가 없습니다."));

        // 지원 정보
        StudyApplyEntity apply = StudyApplyEntity.builder()
                .studyRecruitment(study)
                .user(applicant)
                .result('P')
                .build();

        studyApplyRepository.save(apply);

        return "success";
    }

    // 스터디 모집글 등록
    @Override
    @Transactional
    public String registerStudy(StudySaveRequestDto studyReg, String email) {
        // 토큰 사용하여 user 정보 찾기
        UserEntity writer = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("registerStudy : 해당 유저가 존재하지 않습니다."));

        System.out.println(writer.getEmail());

        // 등록
        studyRepository.save(studyReg.toEntity(writer));
        return "success";
    }

    // 스터디 모집글 수정
    @Override
    @Transactional
    public String updateStudy(final Long stdNo, final StudySaveRequestDto studyReq) {
        StudyRecruitmentEntity study = studyRepository.findById(stdNo)
                .orElseThrow(() -> new IllegalArgumentException("updateStudy : 해당 글이 존재하지 않습니다."));
        study.update(studyReq.getTitle(), studyReq.getContents(), studyReq.getTag(),
                studyReq.getTotal_people(), studyReq.getR_end_date(), studyReq.getS_period());
        return "success";
    }

    // 스터디 모집글 삭제
    @Override
    @Transactional
    public void deleteStudy(final Long stdNo) {
        studyRepository.deleteById(stdNo);
    }

    // 스터디 스크랩 저장
    @Override
    @Transactional
    public String saveStudyScrap(String myEmail, Long stdNo) {
        // 내 스터디 모집글인지 확인
        List<Long> myStudyNumList = studyRepository.findStdNumsByMyEmail(myEmail);
        if(myStudyNumList.contains(stdNo)) {
            return "내 글엔 스크랩할 수 없습니다.";
        }
        else {
            studyScrapRepository.save(StudyScrapEntity.builder()
                    .email(myEmail)
                    .s_no(stdNo)
                    .build());

            return "success";
        }
    }

    // 스터디 스크랩 취소
    @Transactional
    @Override
    public String deleteStudyScrap(String email, Long stdNo) {
        // 스크랩 정보 찾기
        Optional<StudyScrapEntity> myScrap = studyScrapRepository.findStudyScrapEntityByEmailAndAndS_no(email, stdNo);

        if(myScrap.isPresent()) {
            studyScrapRepository.deleteById(myScrap.get().getScrapNo());
            return "success";
        } else
            return "스크랩 정보가 없습니다.";
    }

    // 스터디 스크랩 정보 불러오기
    @Override
    public List<Long> findStudyScrapInfo(String email) {
        return studyScrapRepository.findMyStudyScrapInfo(email);
    }

}
