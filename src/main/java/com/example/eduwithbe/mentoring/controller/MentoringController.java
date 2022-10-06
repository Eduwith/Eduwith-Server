package com.example.eduwithbe.mentoring.controller;

import com.example.eduwithbe.mentoring.dto.MentoringRecruitUpdateDto;
import com.example.eduwithbe.mentoring.dto.ResultResponse;
import com.example.eduwithbe.mentoring.repository.MentoringRepository;
import com.example.eduwithbe.mentoring.service.MentoringLogService;
import com.example.eduwithbe.mentoring.service.MentoringService;
import com.example.eduwithbe.security.JwtTokenProvider;
import com.example.eduwithbe.user.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"MentoringController"})
@RequiredArgsConstructor
@RestController
public class MentoringController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MentoringRepository mentoringRepository;
    private final MentoringService mentoringService;
    private final UserRepository userRepository;

    @ApiOperation(value = "멘토링 상태 수정(진행중->완료)")
    @PatchMapping(value = "/mentoring/{mentoring_no}/state")
    public ResultResponse updateMentoringRecruit(@PathVariable Long mentoring_no) {
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setResult(mentoringService.updateMentoringState(mentoring_no));

        return resultResponse;
    }
}
