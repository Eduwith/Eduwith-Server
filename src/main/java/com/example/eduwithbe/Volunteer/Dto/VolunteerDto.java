package com.example.eduwithbe.Volunteer.Dto;

import com.example.eduwithbe.Volunteer.domain.VolunteerEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VolunteerDto {
    private Long v_no; // 봉사 글 번호
    private String title; // 제목
    private String contents; //내용
    private String address; //주소
    private Date r_start_date; // 모집 시작 날짜
    private Date r_end_date; // 모집 종료 날짜
    private Date v_start_date; // 봉사 시작 날짜
    private Date v_end_date; // 봉사 종료 날짜
    private int total_people; // 모집 인원
    private int current_people; // 현재 인원
    private String url; // 이동할 주소
    private char recruitYN; // 모집완료 여부

    // Entity -> Dto
    public VolunteerDto(VolunteerEntity entity) {
        this.v_no = entity.getV_no();
        this.title = entity.getTitle();
        this.contents = entity.getContents();
        this.address = entity.getAddress();
        this.r_start_date = entity.getR_start_date();
        this.r_end_date = entity.getR_end_date();
        this.v_start_date = entity.getV_start_date();
        this.v_end_date = entity.getV_end_date();
        this.total_people = entity.getTotal_people();
        this.current_people = entity.getCurrent_people();
        this.url = entity.getUrl();
        this.recruitYN = entity.getRecruitYN();
    }
}
