package com.example.eduwithbe.Study.Domain;

import com.example.eduwithbe.user.domain.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study_apply")
@Entity
public class StudyApplyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long apply_no;  // 신청 번호

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "s_no", nullable = false)
    private StudyRecruitmentEntity studyRecruitment;    // 신청한 스터디 번호

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email", nullable = false)
    private UserEntity user;    // 신청자 이메일

    @Column(nullable = false)
    private char result;    // 수락, 거부, 디폴트 : P

    @Builder
    public StudyApplyEntity(StudyRecruitmentEntity studyRecruitment, UserEntity user, char result) {
        this.studyRecruitment = studyRecruitment;
        this.user = user;
        this.result = result;
    }
}
