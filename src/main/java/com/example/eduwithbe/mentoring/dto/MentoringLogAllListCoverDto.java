package com.example.eduwithbe.mentoring.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MentoringLogAllListCoverDto {
    private List<MentoringLogAllListDto> mentor;
    private List<MentoringLogAllListDto> mentee;
}
