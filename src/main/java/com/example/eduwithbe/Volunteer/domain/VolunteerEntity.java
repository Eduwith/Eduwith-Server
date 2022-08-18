package com.example.eduwithbe.Volunteer.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.sql.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="volunteer_list")
@Entity
public class VolunteerEntity {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long v_no; // 봉사 글 번호

    @Column(nullable = false, length = 30)
    private String title; // 제목

    @Column(nullable = false, length = 200)
    private String contents; //내용

    @Column(length = 50)
    private String address; //주소

    @Column(nullable = false)
    private Date r_start_date; // 모집 시작 날짜

    @Column(nullable = false)
    private Date r_end_date; // 모집 종료 날짜

    @Column(nullable = false)
    private Date v_start_date; // 봉사 시작 날짜

    @Column(nullable = false)
    private Date v_end_date; // 봉사 종료 날짜

    @Column(nullable = false)
    private int total_people; // 모집 인원

    @Column(nullable = false)
    @ColumnDefault("0")
    private int current_people; // 현재 인원

    @Column(nullable = false)
    private String url; // 이동할 주소

    @Column(nullable = false)
    private char recruitYN; // 모집완료 여부

    public void setRecruitYN(char result) {
        this.recruitYN = result;
    }
}