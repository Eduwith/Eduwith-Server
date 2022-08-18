package com.example.eduwithbe.Study.Service;

import com.example.eduwithbe.Study.Domain.StudyApplyEntity;
import com.example.eduwithbe.Study.Domain.StudyRecruitmentEntity;
import com.example.eduwithbe.Study.Domain.StudyScrapEntity;
import com.example.eduwithbe.Study.Dto.StudySaveRequestDto;
import com.example.eduwithbe.Study.Dto.StudyRecruitDto;
import com.example.eduwithbe.Study.Repository.StudyApplyRepository;
import com.example.eduwithbe.Study.Repository.StudyRepository;
import com.example.eduwithbe.Study.Repository.StudyScrapRepository;
import com.example.eduwithbe.mappers.StudyMapper;
import com.example.eduwithbe.user.domain.UserEntity;
import com.example.eduwithbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
    private final StudyMapper studyMapper;


//    @Override
//// 모든 스터디 모집글 조회 & 태그 검색 (With. Pagination)
//    public Map<String, Object> getAllStudies(CommonParams params) {
//        // 모집글 수 조회
//        int count = studyMapper.count(params);
//
//        // 등록된 모집글이 없는 경우, 로직 종료
//        if(count < 1) {
//            return Collections.emptyMap();
//        }
//
//        // 페이지네이션 정보 계산
//        Pagination pagination = new Pagination(count, params);
//        params.setPagination(pagination);
//
//        // 모집글 리스트 조회
//        List<StudyRecruitDto> list = studyMapper.findAll(params);
//
//        // 데이터 반환
//        Map<String, Object> response = new HashMap<>();
//        response.put("params", params);
//        response.put("list", list);
//        return response;
//    }


    // 전체 목록 조회
    @Override
    public List<StudyRecruitDto> findAllStudies() {
        List<StudyRecruitmentEntity> studyList = studyRepository.findAll();
        return studyList.stream()
                .map(StudyRecruitDto::new)
                .collect(Collectors.toList());
    }
//    @Override
//    public List<StudyResponseDto> getAllStudies() {
//        PageRequest pageRequest = PageRequest.of(0,6);
//        return studyRepository.findAll(pageRequest).stream().map(StudyResponseDto::new).collect(Collectors.toList());
//    }

    // 모집글 상세정보 조회
    @Override
    public StudyRecruitDto findStudyByNo(final Long stdNo) {
        StudyRecruitmentEntity entity = studyRepository.findById(stdNo).orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));
        return new StudyRecruitDto(entity);
    }

    // 스터디 태그 검색
    @Override
    public List<StudyRecruitDto> findStudiesByTag(String keyword) {
        List<StudyRecruitmentEntity> studyRecruitmentEntities = studyRepository.findByTagContaining(keyword);
        return studyRecruitmentEntities.stream()
                .map(StudyRecruitDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String saveStudyApply(String email, Long s_no) {
        // 신청자 정보, 신청한 스터디의 정보 가져오기
        UserEntity applicant = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("saveStudyApply : 사용자가 없습니다."));
        StudyRecruitmentEntity study = studyRepository.findById(s_no).orElseThrow(() -> new IllegalArgumentException("saveStudyApply : 스터디가 없습니다."));

        // 지원 정보
        StudyApplyEntity apply = StudyApplyEntity.builder()
                .studyRecruitment(study)
                .user(applicant)
                .result('P')
                .build();

        // 해당 모집글의 신청현황 업데이트
        study.addStudyApplies(apply);

        // 스터디 지원 정보 저장
        studyApplyRepository.save(apply);

        return "success";
    }


    // 스터디 모집글 등록
    @Override
    @Transactional
    public Long registerStudy(final StudySaveRequestDto studyReg, String email) {
        // 토큰 사용하여 user 정보 찾기
        UserEntity writer = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("registerStudy : 해당 유저가 존재하지 않습니다."));

        System.out.println(writer.getEmail());

        // 등록
        StudyRecruitmentEntity myStudyEntity = studyReg.toEntity(writer);
        writer.addStudyRecruitment(myStudyEntity);

        return studyRepository.save(myStudyEntity).getS_no();
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

    // 스터디 스크랩 정보 저장
    @Override
    public String saveStudyScrap(String myEmail, Long stdNo) {
        // 스크랩하려는 모집글의 번호 가져오기
        Long s_no = studyRepository.findById(stdNo)
                .orElseThrow(() -> new IllegalArgumentException("saveStudyScrap : 해당 글이 존재하지 않습니다."))
                .getS_no();

        // 내 스터디 모집글인지 확인
        List<Long> stdNumsList = studyRepository.findStdNumsByMyEmail(myEmail);
        if(stdNumsList.contains(s_no)) {
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
}
