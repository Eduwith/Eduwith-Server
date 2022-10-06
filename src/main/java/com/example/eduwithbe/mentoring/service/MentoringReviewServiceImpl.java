package com.example.eduwithbe.mentoring.service;

import com.example.eduwithbe.mentoring.domain.MentoringEntity;
import com.example.eduwithbe.mentoring.domain.MentoringReviewEntity;
import com.example.eduwithbe.mentoring.dto.MentoringReviewSaveDto;
import com.example.eduwithbe.mentoring.dto.MentoringReviewUserSaveDto;
import com.example.eduwithbe.mentoring.repository.MentoringRecruitmentRepository;
import com.example.eduwithbe.mentoring.repository.MentoringRepository;
import com.example.eduwithbe.mentoring.repository.MentoringReviewRepository;
import com.example.eduwithbe.user.domain.UserEntity;
import com.example.eduwithbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MentoringReviewServiceImpl implements MentoringReviewService {

    @Autowired
    private final MentoringReviewRepository mentoringReviewRepository;

    @Autowired
    private final MentoringRepository mentoringRepository;

    @Autowired
    private final UserRepository userRepository;

    //멘토링 리뷰 생성
    public String saveMentoringReview(String email, MentoringReviewSaveDto saveDto){
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다." + email));
        MentoringEntity mentoringEntity = mentoringRepository.findById(saveDto.getMentoring_no()).orElseThrow(() -> new IllegalArgumentException("해당 멘토링이 존재하지 않습니다." + saveDto.getMentoring_no()));

        if(Objects.equals(mentoringEntity.getWriter().getEmail(), userEntity.getEmail())) return "FAILURE";

        if(Objects.equals(mentoringEntity.getState(), "진행중")) return "FAILURE";

        MentoringReviewUserSaveDto mentoringReviewSaveDto = new MentoringReviewUserSaveDto();
        mentoringReviewSaveDto.setMentoring_no(mentoringEntity);
        mentoringReviewSaveDto.setReviewer(userEntity);
        mentoringReviewSaveDto.setWriter(mentoringEntity.getWriter());
        mentoringReviewSaveDto.setStar(saveDto.getStar());
        mentoringReviewSaveDto.setReview(saveDto.getReview());

        MentoringReviewEntity mentoringReviewEntity = mentoringReviewSaveDto.toEntity();
        mentoringReviewRepository.save(mentoringReviewEntity);
        List<MentoringReviewEntity> reviewEntityList = mentoringReviewRepository.findByStar(mentoringReviewEntity.getWriter().getEmail());

        int num = 0;
        Float sum = 0F;
        for (MentoringReviewEntity entity: reviewEntityList) {
            sum+=entity.getStar();
            num++;
        }

        userRepository.updateStarByUser(mentoringReviewEntity.getWriter().getEmail(), Math.round((sum/num)*10)/10.0F);
        //mentoringRepository.updateStarByMentoring(mentoringEntity.getMentoring_no(), saveDto.getStar());

        return "SUCCESS";
    }
}
