package com.example.eduwithbe.mentoring.service;

import com.example.eduwithbe.mentoring.domain.MentoringApplyEntity;
import com.example.eduwithbe.mentoring.domain.MentoringEntity;
import com.example.eduwithbe.mentoring.domain.MentoringRecruitmentEntity;
import com.example.eduwithbe.mentoring.dto.MentoringSaveDto;
import com.example.eduwithbe.mentoring.repository.MentoringApplyRepository;
import com.example.eduwithbe.mentoring.repository.MentoringRecruitmentRepository;
import com.example.eduwithbe.mentoring.repository.MentoringRepository;
import com.example.eduwithbe.user.domain.UserEntity;
import com.example.eduwithbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MentoringServiceImpl implements MentoringService {

    @Autowired
    private final MentoringRepository mr;

    @Autowired
    private final UserRepository ur;

    @Autowired
    private final MentoringRecruitmentRepository mrr;

    @Autowired
    private final MentoringApplyRepository mar;

    //멘토링 신청 수락
    @Override
    public String saveMentoring(String email, Long m_no, Long apply_no) {
        Optional<UserEntity> user = ur.findByEmail(email);
        MentoringRecruitmentEntity mentoringRecruitment = mrr.findById(m_no).orElseThrow(() -> new IllegalArgumentException("신청 실패: 해당 멘토링이 존재하지 않습니다." + m_no));
        MentoringApplyEntity mentoringApplyRecruitment = mar.findById(apply_no).orElseThrow(() -> new IllegalArgumentException("신청 실패: 해당 신청이 존재하지 않습니다." + apply_no));
        Optional<UserEntity> applyUser = ur.findByEmail(mentoringApplyRecruitment.getEmail());

        MentoringSaveDto mentoringSaveDto = new MentoringSaveDto();

        mentoringSaveDto.setApplicant(applyUser.get());
        mentoringSaveDto.setM_no(mentoringRecruitment);
        mentoringSaveDto.setWriter(mentoringApplyRecruitment.getM_no().getUser());

        MentoringEntity apply = mentoringSaveDto.toEntity();
        mr.save(apply);

        mar.delete(mentoringApplyRecruitment);

        //MentoringApplyEntity mentoringApply = mr.save(dto.toEntity());
        return "OK";
    }

    //멘토링 신청 거절, 신청 취소
    @Override
    public String deleteMentoring(Long apply_no) {
        MentoringApplyEntity mentoringApply = mar.findById(apply_no).orElseThrow(() -> new IllegalArgumentException("신청 실패: 해당 신청이 존재하지 않습니다." + apply_no));

        mar.delete(mentoringApply);

        return "OK";
    }
}
