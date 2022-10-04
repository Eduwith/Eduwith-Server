package com.example.eduwithbe.mentoring.dto;

import com.example.eduwithbe.mentoring.domain.MentoringApplyEntity;
import com.example.eduwithbe.mentoring.domain.MentoringRecruitmentEntity;
import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.stream.Collectors;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MentoringApplyEmailDto {

    private Long apply_no;
    private MentoringRecruitDto m_no;
    private String email;

    public MentoringApplyEmailDto(MentoringApplyEntity ma) {
        this.apply_no = ma.getApply_no();
        this.m_no = new MentoringRecruitDto(ma.getM_no());
        this.email = ma.getEmail();
    }
}
