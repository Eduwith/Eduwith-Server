package com.example.eduwithbe.Volunteer.Dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VolunteerScrapInfoDto {
    private char scrapYn;   // 스크랩 저장&취소 정보
    private Long v_no;
}
