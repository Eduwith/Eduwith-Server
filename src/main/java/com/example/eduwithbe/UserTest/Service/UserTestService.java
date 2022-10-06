package com.example.eduwithbe.UserTest.Service;

import com.example.eduwithbe.UserTest.DTO.UserTestSaveDto;

public interface UserTestService {
    String saveTestResult(String email, UserTestSaveDto utDto);
}
