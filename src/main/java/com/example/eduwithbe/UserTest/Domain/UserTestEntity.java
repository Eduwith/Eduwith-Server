package com.example.eduwithbe.UserTest.Domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "user_test")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserTestEntity {
    @Id
    @Column(nullable = false)
    private String email;   // 사용자 email

    @Column(nullable = false, length = 10)
    private String mbti;    // 학습 MBTI

    @Column(nullable = false, length = 100)
    private String animal;  // 동물

    @Column(nullable = false, length = 10)
    private String mate_mbti;   // 짝 mbti

    @Builder
    public UserTestEntity(String email, String mbti, String animal, String mate_mbti) {
        this.email = email;
        this.mbti = mbti;
        this.animal = animal;
        this.mate_mbti = mate_mbti;
    }
}
