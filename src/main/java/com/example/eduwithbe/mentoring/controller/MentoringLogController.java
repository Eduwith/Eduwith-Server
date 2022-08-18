package com.example.eduwithbe.mentoring.controller;

import com.example.eduwithbe.mentoring.domain.MentoringEntity;
import com.example.eduwithbe.mentoring.domain.MentoringLogEntity;
import com.example.eduwithbe.mentoring.domain.MentoringRecruitmentEntity;
import com.example.eduwithbe.mentoring.dto.*;
import com.example.eduwithbe.mentoring.repository.MentoringRepository;
import com.example.eduwithbe.mentoring.service.MentoringLogService;
import com.example.eduwithbe.mentoring.service.MentoringRecruitmentService;
import com.example.eduwithbe.security.JwtTokenProvider;
import com.example.eduwithbe.user.domain.UserEntity;
import com.example.eduwithbe.user.dto.UserMentoringApplyDetailDTO;
import com.example.eduwithbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping(value = "/mentoring/log")
@RequiredArgsConstructor
@RestController
public class MentoringLogController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MentoringLogService mentoringLogService;
    private final MentoringRecruitmentService mentoringRecruitmentService;
    private final MentoringRepository mentoringRepository;
    private final UserRepository userRepository;

    //로그 글 저장
    @PostMapping(value = "/save")
    public ResultResponse saveMentoringLog(HttpServletRequest request, @RequestBody MentoringLogSaveDto mentoringLogSaveDto) {
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        Optional<MentoringEntity> mentoringEntity = mentoringRepository.findById(mentoringLogSaveDto.getMentoring_no());
        mentoringLogService.saveMentoringLog(user, mentoringEntity.get(), mentoringLogSaveDto);

        return new ResultResponse();
    }

    //로그 글 상세 보기
    @GetMapping(value = "/{log_no}")
    public MentoringLogGetIdDto findOneMentoringLog(@PathVariable Long log_no) {
        MentoringLogEntity mentoringLogEntity = mentoringLogService.findByMentoringLogId(log_no);

        return MentoringLogGetIdDto.builder().mentoringLogEntity(mentoringLogEntity).build();
    }

    //로그 글 전첼 리스트
    @GetMapping(value = "/list")
    public List<MentoringLogAllList> findByIdMentoringLog(HttpServletRequest request) {
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        UserEntity loginUser = userRepository.findByEmail(user).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다." + user));

        List<MentoringLogAllList> list = new ArrayList<>();

        for(int i = 0; i < loginUser.getMentoringEntities().size(); i++) {
            Optional<MentoringEntity> mentoringEntity = mentoringRepository.findById(loginUser.getMentoringEntities().get(i).getMentoring_no());
            List<MentoringLogGetIdDto> mentoringLogGetIdDtos = mentoringLogService.findAllMentoringLog(mentoringEntity.get().getMentoring_no());
            UserEntity userEntity = userRepository.findByEmail(mentoringEntity.get().getEmail()).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다." + mentoringEntity.get().getEmail()));
            UserMentoringApplyDetailDTO mentee = new UserMentoringApplyDetailDTO();
            mentee.setEmail(userEntity.getEmail());
            mentee.setAge(userEntity.getAge());
            mentee.setName(userEntity.getName());

            list.add(new MentoringLogAllList(mentoringEntity.get().getMentoring_no(),mentoringEntity.get().getM_no().getTitle(), mentee, mentoringLogGetIdDtos));
        }

        return list;
    }


    //로그 글 수정
    @PatchMapping(value = "/{log_no}")
    public ResultResponse updateMentoringLog(@PathVariable Long log_no, @RequestBody MentoringLogUpdateDto updateDto) {
        mentoringLogService.updateMentoringLog(log_no, updateDto);

        return new ResultResponse();
    }

    //로그 글 삭제
    @DeleteMapping(value = "/{log_no}")
    public ResultResponse deleteMentoringLog(@PathVariable Long log_no) {
        MentoringLogEntity mentoringLogEntity = mentoringLogService.findByMentoringLogId(log_no);
        mentoringLogService.deleteMentoringLog(mentoringLogEntity);

        return new ResultResponse();
    }
}
