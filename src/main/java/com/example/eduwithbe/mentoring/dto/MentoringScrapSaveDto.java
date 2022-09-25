package com.example.eduwithbe.mentoring.dto;

import com.example.eduwithbe.mentoring.domain.MentoringApplyEntity;
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
public class MentoringScrapSaveDto {
    private Long scrap_no;
    private MentoringRecruitmentEntity m_no;
    private UserEntity user;

    public MentoringScrapEntity toEntity(){
        return MentoringScrapEntity.builder()
                .m_no(m_no)
                .scrap_no(scrap_no)
                .user(user)
                .build();
    }
}
