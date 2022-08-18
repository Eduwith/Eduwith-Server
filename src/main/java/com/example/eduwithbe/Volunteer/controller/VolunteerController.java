package com.example.eduwithbe.Volunteer.controller;

import com.example.eduwithbe.Volunteer.Dto.VolunteerDto;
import com.example.eduwithbe.Volunteer.Dto.VolunteerScrapInfoDto;
import com.example.eduwithbe.Volunteer.service.VolunteerService;
import com.example.eduwithbe.security.JwtTokenProvider;
import com.example.eduwithbe.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/volunteers")
public class VolunteerController {
    private final VolunteerService volunteerService;
    private final JwtTokenProvider jwtTokenProvider;

    // 전체 봉사 목록 리스트 조회
    @GetMapping("")
    public List<VolunteerDto> volunteerEntityList() {
        return volunteerService.findVolunteers();
    }

    // 상세 정보 조회
    @GetMapping("/view/{no}")
    public VolunteerDto volunteerDetailView(@PathVariable Long no) {
        return volunteerService.volunteerDetailView(no);
    }

    // 봉사 스크랩
    @PostMapping("/view/{no}")
    public String saveVolunteerScrap(HttpServletRequest request,
                                     @RequestBody VolunteerScrapInfoDto volunteerScrapSaveDto) {
        String userEmail = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));

        return volunteerService.saveVolunteerScrap(volunteerScrapSaveDto, userEmail);
    }

}