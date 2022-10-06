package com.example.eduwithbe.Study.Domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "studying")
@Entity
public class StudyingEntity {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studyingNo;

    @ManyToOne
    @JoinColumn(nullable = false, name = "s_no")
    private StudyRecruitmentEntity studyRecruitment;

    @Column(nullable = false)
    private String email;

    @Builder
    public StudyingEntity(StudyRecruitmentEntity studyRecruitment, String email) {
        this.studyRecruitment = studyRecruitment;
        this.email = email;
    }
}
