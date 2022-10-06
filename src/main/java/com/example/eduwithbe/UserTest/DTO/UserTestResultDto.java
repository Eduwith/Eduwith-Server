package com.example.eduwithbe.UserTest.DTO;

import com.example.eduwithbe.UserTest.Domain.UserTestEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTestResultDto {
    private String mbti;    // 나의 MBTI
    private String animal;  // 동물

    public UserTestEntity toEntity(String userEmail, String mate_mbti) {
        return UserTestEntity.builder()
                .email(userEmail)
                .mbti(mbti)
                .animal(animal)
                .mate_mbti(mate_mbti)
                .build();
    }

    @Builder
    public UserTestResultDto(String mbti, String animal) {
        this.mbti = mbti;
        this.animal = animal;
    }
}
