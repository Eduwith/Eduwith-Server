package com.example.eduwithbe.mentoring.controller;

import com.example.eduwithbe.mentoring.dto.MentoringRecruitSaveDto;
import com.example.eduwithbe.mentoring.dto.MentoringReviewSaveDto;
import com.example.eduwithbe.mentoring.dto.ResultResponse;
import com.example.eduwithbe.mentoring.service.MentoringRecruitmentService;
import com.example.eduwithbe.mentoring.service.MentoringReviewService;
import com.example.eduwithbe.security.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(tags = {"MentoringReviewController"})
@RequiredArgsConstructor
@RequestMapping(value = "/mentoring")
@RestController
public class MentoringReviewController {
    private final MentoringReviewService mentoringReviewService;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "멘토링 리뷰 작성")
    @PostMapping(value = "/review")
    public ResultResponse saveMentoringRecruit(HttpServletRequest request, @RequestBody MentoringReviewSaveDto saveDto) {
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setResult(mentoringReviewService.saveMentoringReview(user, saveDto));

        return resultResponse;
    }
}
