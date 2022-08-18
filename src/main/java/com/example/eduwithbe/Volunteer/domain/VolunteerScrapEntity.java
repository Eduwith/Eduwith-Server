package com.example.eduwithbe.Volunteer.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "volunteer_scrap")
@Entity
public class VolunteerScrapEntity {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scrapNo;   // 스크랩 번호

    @Column(nullable = false)
    private String email;   // 사용자 이메일

    @Column(nullable = false)
    private Long v_no;  // 봉사글번호

    @Builder
    public VolunteerScrapEntity(String email, Long v_no) {
        this.email = email;
        this.v_no = v_no;
    }
}
