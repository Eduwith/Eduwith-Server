package com.example.eduwithbe.mentoring.dto;

import com.example.eduwithbe.mentoring.domain.MentoringEntity;
import com.example.eduwithbe.user.dto.UserMentoringApplyDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MentoringLogAllList {
    private Long mentoring_no;
    private String m_title;
    private UserMentoringApplyDetailDTO user;
    private List<MentoringLogGetIdDto> logList;
}
