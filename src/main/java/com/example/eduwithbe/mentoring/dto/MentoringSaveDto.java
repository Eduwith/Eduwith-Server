package com.example.eduwithbe.mentoring.dto;

import com.example.eduwithbe.mentoring.domain.MentoringEntity;
import com.example.eduwithbe.mentoring.domain.MentoringRecruitmentEntity;
import com.example.eduwithbe.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MentoringSaveDto {
    private MentoringRecruitmentEntity m_no;
    private String applicant;
    private UserEntity writer;

    public MentoringEntity toEntity(){
        return MentoringEntity.builder()
                .m_no(m_no)
                .applicant(applicant)
                .writer(writer)
                .build();
    }
}

