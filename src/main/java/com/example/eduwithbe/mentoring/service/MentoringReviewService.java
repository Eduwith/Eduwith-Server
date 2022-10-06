package com.example.eduwithbe.mentoring.service;

import com.example.eduwithbe.mentoring.dto.MentoringReviewSaveDto;
import org.springframework.stereotype.Service;

@Service
public interface MentoringReviewService {
    String saveMentoringReview(String email, MentoringReviewSaveDto mentoringReviewSaveDto);
}
