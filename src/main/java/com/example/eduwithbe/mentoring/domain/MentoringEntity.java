package com.example.eduwithbe.mentoring.domain;

import com.example.eduwithbe.user.domain.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name="mentoring")
public class MentoringEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentoring_no")
    private Long mentoring_no;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "m_no")
    private MentoringRecruitmentEntity m_no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private UserEntity writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant")
    private UserEntity applicant;

//    @Column(name = "star") //평점 평균
//    private Float star;

    @JsonIgnore
    @OneToMany(mappedBy = "mentoring_no", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<MentoringLogEntity> mentoringLog = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "mentoring_no", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<MentoringReviewEntity> mentoringReview = new ArrayList<>();
}
