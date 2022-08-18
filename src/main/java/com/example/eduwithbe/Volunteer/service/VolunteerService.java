package com.example.eduwithbe.Volunteer.service;

import com.example.eduwithbe.Volunteer.Dto.VolunteerDto;
import com.example.eduwithbe.Volunteer.Dto.VolunteerScrapInfoDto;

import java.util.List;

public interface VolunteerService {
    List<VolunteerDto> findVolunteers(); // 전체 봉사 모집글 조회

    VolunteerDto volunteerDetailView(Long no); // 특정 모집글 상세 보기

    // 자원봉사 스크랩
    String saveVolunteerScrap(VolunteerScrapInfoDto vs, String myEmail);


}
