package com.example.eduwithbe.mentoring.dto;

import com.example.eduwithbe.mentoring.domain.MentoringRecruitmentEntity;
import com.example.eduwithbe.mentoring.domain.MentoringScrapEntity;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MentoringRecruitDto {
    private Long m_no;
    private String title;
    private String role;
    private String field;
    private String region;
    private int m_period;
    private String way;
    private String keyword;
    private String info;
    private String name;
    private Float star;
    private String scrap;

    @Builder
    public MentoringRecruitDto(MentoringRecruitmentEntity me) {
        this.m_no = me.getM_no();
        this.title = me.getTitle();
        this.role = me.getRole();
        this.field = me.getField();
        this.region = me.getRegion();
        this.m_period = me.getM_period();
        this.way = me.getWay();
        this.keyword = me.getKeyword();
        this.info = me.getInfo();
        this.name = me.getName();
        this.star = me.getUser().getStar();
        this.scrap = scrap;
    }
}
