package com.example.eduwithbe.notice.controller;

import com.example.eduwithbe.mentoring.dto.ResultResponse;
import com.example.eduwithbe.notice.domain.NoticeEntity;
import com.example.eduwithbe.notice.dto.NoticeGetDto;
import com.example.eduwithbe.notice.service.NoticeService;
import com.example.eduwithbe.security.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Api(tags = {"NoticeController"})
@RequestMapping(value = "/notice")
@RequiredArgsConstructor
@RestController
public class NoticeController {

    private final JwtTokenProvider jwtTokenProvider;
    private final NoticeService noticeService;

    @ApiOperation(value = "알림조회")
    @GetMapping(value = "")
    public List<NoticeGetDto> findNoticeByEmail(HttpServletRequest request) {
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        return noticeService.findByEmailNotice(user);
    }

    @ApiOperation(value = "알림 읽음 확인")
    @PatchMapping(value = "/{notice_no}")
    public ResultResponse patchNoticeByEmail(@PathVariable Long notice_no) {
        String s =  noticeService.updateNotice(notice_no);

        ResultResponse resultResponse = new ResultResponse();
        if(Objects.equals(s, "OK")) resultResponse.setResult("SUCCESS");
        else resultResponse.setResult("FAILURE");

        return resultResponse;
    }

    @ApiOperation(value = "알림 삭제")
    @DeleteMapping(value = "/{notice_no}")
    public ResultResponse deleteBoard(@PathVariable Long notice_no) {
        NoticeEntity noticeEntity = noticeService.findByNoticeId(notice_no);
        noticeService.deleteNotice(noticeEntity);

        return new ResultResponse();
    }

}
