package com.example.eduwithbe.mentoring.dto;

import com.example.eduwithbe.mentoring.domain.MentoringRecruitmentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MentoringRecruitUpdateDto {
    private String title;
    private String role;
    private String field;
    private String region;
    private int m_period;
    private String way;
    private String keyword;
    private String info;

    public MentoringRecruitmentEntity toEntity(){
        return MentoringRecruitmentEntity.builder()
                .title(title)
                .role(role)
                .field(field)
                .region(region)
                .m_period(m_period)
                .way(way)
                .keyword(keyword)
                .info(info)
                .build();
    }
}
