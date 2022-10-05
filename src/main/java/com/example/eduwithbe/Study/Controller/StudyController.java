package com.example.eduwithbe.Study.Controller;

import com.example.eduwithbe.Study.Dto.StudySaveRequestDto;
import com.example.eduwithbe.Study.Dto.StudyRecruitDto;
import com.example.eduwithbe.Study.Service.StudyService;
import com.example.eduwithbe.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/studies")
public class StudyController {

    private final StudyService studyService;
    private final JwtTokenProvider jwtTokenProvider;

    // 모든 스터디 모집글 조회
    @GetMapping("") /* default page = 0, size = 10 */
    public Page<StudyRecruitDto> findAllStudies(@PageableDefault(direction = Sort.Direction.DESC) Pageable pageable) {
        return studyService.studyPageList(pageable);
    }

    // 스터디 모집글 상세정보 조회
    @GetMapping("/{stdNo}")
    public StudyRecruitDto findStudyByNo(@PathVariable final Long stdNo) {
        return studyService.findStudyByNo(stdNo);
    }

    // 스터디 신청하기
    @PostMapping("/{stdNo}")
    public Map<String, String> applyStudy(HttpServletRequest request, @PathVariable Long stdNo) {
        // 로그인 사용자 이메일 추출
        String userEmail = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));

        // 서비스 실행 및 응답
        String response = studyService.saveStudyApply(userEmail, stdNo);

        Map<String, String> result = new HashMap<>();
        result.put("result", response);

        return result;
    }

    // 스터디 키워드 검색
    @GetMapping("/search")
    public List<StudyRecruitDto> searchStudyByTag(@RequestParam(value = "keyword", defaultValue = "") String keyword) {
        System.out.println("=======keyword : " + keyword);
        return studyService.findStudiesByTag(keyword);
    }

    // 스터디 모집글 등록 후 목록 화면으로 이동
    @PostMapping("/register")
    public String registerStudy(@RequestBody StudySaveRequestDto studyReq,
                              HttpServletRequest request){
        // 로그인 한 사용자 이메일 추출
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));

        // 스터디 모집글 등록
        studyService.registerStudy(studyReq, user);

        return "success";
    }

    // 스터디 모집글 삭제
    @DeleteMapping("/{stdNo}")
    public void deleteStudy(@PathVariable Long stdNo) {
        studyService.deleteStudy(stdNo);
    }

    // 스터디 스크랩 (저장)
    @PostMapping("/{stdNo}/scrap/save")
    public String saveStudyScrap(HttpServletRequest request, @PathVariable Long stdNo) {
        // 로그인 한 사용자 추출
        String userEmail = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));

        return studyService.saveStudyScrap(userEmail, stdNo);
    }

    // 스터디 스크랩 (취소)
    @DeleteMapping("/{stdNo}/scrap/delete")
    public String deleteStudyScrap(HttpServletRequest request, @PathVariable Long stdNo) {
        String userEmail = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));

        return studyService.deleteStudyScrap(userEmail, stdNo);
    }

    // 스터디 스크랩 정보 불러오기
    @GetMapping("/scrapInfo")
    public List<Long> findStudyScrapInfo(HttpServletRequest request) {
        String userEmail = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));

        return studyService.findStudyScrapInfo(userEmail);
    }
}
