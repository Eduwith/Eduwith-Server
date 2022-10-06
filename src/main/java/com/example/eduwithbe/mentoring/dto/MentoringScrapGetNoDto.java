package com.example.eduwithbe.mentoring.dto;

import com.example.eduwithbe.mentoring.domain.MentoringScrapEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MentoringScrapGetNoDto {
    private Long scrap_no;
    private Long no;
    private String title;
    private String email;

    public MentoringScrapGetNoDto(MentoringScrapEntity mentoringScrap) {
        this.scrap_no = mentoringScrap.getScrap_no();
        this.no = mentoringScrap.getM_no().getM_no();
        this.title = mentoringScrap.getM_no().getTitle();
        this.email = mentoringScrap.getUser().getEmail();
    }
}
