package com.example.eduwithbe.UserTest.Service;

import com.example.eduwithbe.UserTest.DTO.UserTestResultDto;

public interface UserTestService {
    String saveTestResult(String email, UserTestResultDto utDto); // 테스트 결과 저장

    UserTestResultDto findMyTestResult(String email); // 나의 테스트 결과 조회
}
