package com.example.eduwithbe.mentoring.dto;

import com.example.eduwithbe.mentoring.domain.MentoringEntity;
import com.example.eduwithbe.mentoring.domain.MentoringReviewEntity;
import com.example.eduwithbe.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MentoringReviewUserSaveDto {
    private MentoringEntity mentoring_no;
    private UserEntity reviewer;
    private UserEntity writer;
    private Float star;
    private String review;

    public MentoringReviewEntity toEntity() {
        return MentoringReviewEntity.builder()
                .mentoring_no(mentoring_no)
                .writer(writer)
                .reviewer(reviewer)
                .star(star)
                .review(review)
                .build();
    }
}
