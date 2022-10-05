package com.example.eduwithbe.Study.Domain;

import com.example.eduwithbe.user.domain.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study_recruitment")
@Entity
public class StudyRecruitmentEntity {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long s_no; // 스터디 글 번호

    @Column(nullable = false, length = 20)
    private String title; // 제목

    @Column(nullable = false, length = 200)
    private String contents; // 내용

    @Column(nullable = false, length = 50)
    private String tag; // 태그

    @Column(nullable = false)
    private int total_people; // 모집 인원

    @Column(nullable = false)
    private int current_people; // 현재 인원(수락 확정)

    @Column(nullable = false)
    private Date r_end_date; // 모집 마감 기한

    @Column(nullable = false)
    private int s_period; // 스터디 기간

    @Column(nullable = false)
    private char recruitYN; //모집 완료 여부

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(nullable = false, name = "email")
    private UserEntity user;    // 작성자 이메일

    @Builder
    public StudyRecruitmentEntity(String title, String contents, String tag, int total_people, int current_people,
                                 Date r_end_date, int s_period, char recruitYN, UserEntity user) {
        this.title = title;
        this.contents = contents;
        this.tag = tag;
        this.total_people = total_people;
        this.current_people = current_people; // 모집글 생성 시 현재 확정 인원은 0
        this.r_end_date = r_end_date;
        this.s_period = s_period;
        this.recruitYN = recruitYN;
        this.user = user;
    }

    // insert 되기 전 실행됨
    @PrePersist
    public void prePersist() {
        this.current_people = 0;
        this.recruitYN = 'N';
    }

    // 모집글 수정
    public void update(String title, String contents, String tag, int total_people,
                       Date r_end_date, int s_period) {
        this.title = title;
        this.contents = contents;
        this.tag = tag;
        this.total_people = total_people;
        this.r_end_date = r_end_date;
        this.s_period = s_period;
    }
}
