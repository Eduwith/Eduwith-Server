package com.example.eduwithbe.Study.Controller;

import com.example.eduwithbe.Study.Dto.*;
import com.example.eduwithbe.Study.Service.StudyAdminService;
import com.example.eduwithbe.Study.Service.StudyService;
import com.example.eduwithbe.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/studyAdmin")
@RequiredArgsConstructor
public class StudyAdminController{
    private final StudyAdminService studyAdminService;
    private final StudyService studyService;
    private final JwtTokenProvider jwtTokenProvider;

    // 내가 모집한 스터디 확인
    @GetMapping("/myStudyRecruit")
    public List<StudyRecruitDto> findMyStudyRecruits(HttpServletRequest request) {
        // 사용자 이메일 추출
        String myEmail = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));

        return studyAdminService.findMyStudyRecruits(myEmail);
    }

    // 내가 모집한 스터디 수정 - 기존 내용 불러오기
    @GetMapping("/myStudyRecruit/{stdNo}/update")
    public StudyRecruitDto update(@PathVariable final Long stdNo) {
        return studyService.findStudyByNo(stdNo);
    }

    // 내가 모집한 스터디 수정 - 수정하기
    @PutMapping("/myStudyRecruit/{stdNo}/update")
    public String updateStudyInfo(@PathVariable final Long stdNo, @RequestBody StudySaveRequestDto studyReq) {
        return studyService.updateStudy(stdNo, studyReq);
    }

    // 스터디 마감하기
    @PutMapping("/myStudyRecruit/{stdNo}/finish")
    public String finishStudy(@PathVariable final Long stdNo) {
        return studyAdminService.finishStudy(stdNo);
    }

    // 스터디 신청자 정보 확인
    @GetMapping("myStudyRecruit/{stdNo}/applicant/{applyNo}")
    public UserDetailDto findApplicantDetail(@PathVariable Long stdNo, @PathVariable Long applyNo) {
        return studyAdminService.findUserDetail(applyNo);
    }

    // 스터디 신청 수락
    @PostMapping("myStudyRecruit/{stdNo}/apply/{applyNo}")
    public String saveStudyApplyAccept(HttpServletRequest request, @PathVariable Long stdNo,
                                       @PathVariable Long applyNo) {
        return studyAdminService.saveStudying(stdNo, applyNo);
    }

    // 신청자 거절
    @DeleteMapping("myStudyRecruit/{stdNo}/apply/{applyNo}")
    public String refuseStudyApply(@PathVariable Long stdNo,@PathVariable Long applyNo) {
        return studyAdminService.deleteStudyApply(applyNo);
    }


}
