package com.example.eduwithbe.mentoring.dto;

import com.example.eduwithbe.mentoring.domain.MentoringRecruitmentEntity;
import com.example.eduwithbe.mentoring.domain.MentoringScrapEntity;
import com.example.eduwithbe.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MentoringScrapGetDto {
    private Long scrap_no;
    private Long m_no;
    private String email;

    public MentoringScrapGetDto(MentoringScrapEntity mentoringScrap) {
        this.scrap_no = mentoringScrap.getScrap_no();
        this.m_no = mentoringScrap.getM_no().getM_no();
        this.email = mentoringScrap.getUser().getEmail();
    }
}
