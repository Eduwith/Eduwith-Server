package com.example.eduwithbe.UserTest.Controller;

import com.example.eduwithbe.UserTest.Service.UserTestService;
import com.example.eduwithbe.UserTest.DTO.UserTestResultDto;
import com.example.eduwithbe.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userTest")
public class UserTestController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserTestService userTestService;

    // 학습유형 테스트 결과 저장
    @PostMapping("")
    public Map<String, String> saveStudyTypeTestResult(HttpServletRequest request, @RequestBody UserTestResultDto userTestResultDto) {
        // 현재 로그인한 사용자 이메일 추출
        String userEmail = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));

        // 테스트 결과 저장
        String response = userTestService.saveTestResult(userEmail, userTestResultDto);

        Map<String, String> result = new HashMap<>();
        result.put("studyTypeTest result", response);

        return result;
    }

    // 나의 테스트 결과 조회
    @GetMapping("/myResult")
    public UserTestResultDto findMyTestResult(HttpServletRequest request) {
        // 현재 로그인한 사용자 이메일 추출
        String userEmail = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));

        return userTestService.findMyTestResult(userEmail);
    }
}
