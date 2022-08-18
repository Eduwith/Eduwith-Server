package com.example.eduwithbe.mentoring.domain;

import com.example.eduwithbe.mentoring.dto.MentoringRecruitSaveDto;
import com.example.eduwithbe.user.domain.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name="mentoringRecruitment")
public class MentoringRecruitmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "m_no")
    private Long m_no;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 1)
    private String role;

    @Column(nullable = false, length = 30)
    private String field;

    @Column(nullable = false, length = 50)
    private String region;

    @Column(nullable = false)
    private int m_period;

    @Column(nullable = false, length = 4)
    private String way;

    @Column(nullable = false, length = 50)
    private String keyword;

    @Column(nullable = false, length = 200)
    private String info;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private UserEntity user;

    @JsonIgnore
    @OneToMany(mappedBy = "m_no", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<MentoringScrapEntity> mentoringScrap = new ArrayList<>();

    @OneToMany(mappedBy = "m_no", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<MentoringApplyEntity> mentoringApply = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "m_no", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<MentoringEntity> mentoring = new ArrayList<>();

    public void updateBoard(MentoringRecruitSaveDto dto){
        this.title = dto.getTitle();
        this.role = dto.getRole();
        this.field = dto.getField();
        this.region = dto.getRegion();
        this.m_period = dto.getM_period();
        this.way = dto.getWay();
        this.keyword = dto.getKeyword();
        this.info = dto.getInfo();
    }
}
