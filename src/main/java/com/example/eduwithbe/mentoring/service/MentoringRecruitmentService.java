package com.example.eduwithbe.mentoring.service;

import com.example.eduwithbe.mentoring.domain.MentoringRecruitmentEntity;
import com.example.eduwithbe.mentoring.dto.MentoringRecruitListDto;
import com.example.eduwithbe.mentoring.dto.MentoringRecruitSaveDto;
import com.example.eduwithbe.mentoring.dto.MentoringRecruitSearchDto;
import com.example.eduwithbe.mentoring.dto.MentoringRecruitUpdateDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MentoringRecruitmentService {

    void saveMentoringRecruit(String user, MentoringRecruitSaveDto dto);

    List<MentoringRecruitListDto> findByTitleContaining(String keyword);

    MentoringRecruitmentEntity findByMentoringRecruitId(Long m_no);

    List<MentoringRecruitListDto> findAllMentoringRecruitment();

    void updateMentoringRecruitment(Long m_no, MentoringRecruitUpdateDto updateDto);

    void deleteMentoringRecruit(MentoringRecruitmentEntity board);

    List<MentoringRecruitSearchDto> findByEmailMentoringMentor(String email);

    List<MentoringRecruitSearchDto> findByEmailMentoringMentee(String email);

    List<MentoringRecruitListDto> findByMentoringMentor();

    List<MentoringRecruitListDto> findByMentoringMentee();

    List<MentoringRecruitSearchDto> findByDistance(String email);

    List<MentoringRecruitListDto> findByFilter(List<String> field, List<String> region, List<Integer> m_period, List<String> way);
}
