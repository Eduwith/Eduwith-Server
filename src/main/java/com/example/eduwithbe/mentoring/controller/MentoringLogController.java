package com.example.eduwithbe.mentoring.controller;

import com.example.eduwithbe.mentoring.domain.MentoringEntity;
import com.example.eduwithbe.mentoring.domain.MentoringLogEntity;
import com.example.eduwithbe.mentoring.dto.*;
import com.example.eduwithbe.mentoring.repository.MentoringRepository;
import com.example.eduwithbe.mentoring.service.MentoringLogService;
import com.example.eduwithbe.security.JwtTokenProvider;
import com.example.eduwithbe.user.domain.UserEntity;
import com.example.eduwithbe.user.dto.UserMentoringApplyDetailDTO;
import com.example.eduwithbe.user.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Api(tags = {"MentoringLogController"})
@RequiredArgsConstructor
@RestController
//@RequestMapping("/mentoring/log")
public class MentoringLogController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MentoringLogService mentoringLogService;
    private final MentoringRepository mentoringRepository;
    private final UserRepository userRepository;

    @ApiOperation(value = "로그 글 저장")
    @PostMapping(value = "/mentoring/log/save")
    public ResultResponse saveMentoringLog(HttpServletRequest request, @RequestBody MentoringLogSaveDto mentoringLogSaveDto) {
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        Optional<MentoringEntity> mentoringEntity = mentoringRepository.findById(mentoringLogSaveDto.getMentoring_no());
        mentoringEntity.ifPresent(mentoring -> mentoringLogService.saveMentoringLog(user, mentoring, mentoringLogSaveDto));

        return new ResultResponse();
    }

    @ApiOperation(value = "로그 글 상세보기")
    @GetMapping(value = "/mentoring/log/{log_no}")
    public MentoringLogGetIdDto findOneMentoringLog(@PathVariable Long log_no) {
        MentoringLogEntity mentoringLogEntity = mentoringLogService.findByMentoringLogId(log_no);

        return MentoringLogGetIdDto.builder().mentoringLogEntity(mentoringLogEntity).build();
    }

    @ApiOperation(value = "로글 글 전체 리스트")
    @GetMapping(value = "/mentoring/log")
    public List<MentoringLogAllListDto> findByIdMentoringLog(HttpServletRequest request) {
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        UserEntity loginUser = userRepository.findByEmail(user).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다." + user));

        List<MentoringLogAllListDto> mentoringLogAllLists = new ArrayList<>();

        for(int i = 0; i < loginUser.getMentoringEntities().size(); i++) {
            Optional<MentoringEntity> mentoringEntity = mentoringRepository.findById(loginUser.getMentoringEntities().get(i).getMentoring_no());
            List<MentoringLogGetIdDto> mentoringLogGetIdDto = new ArrayList<>();
            UserEntity userEntity = new UserEntity();
            if(mentoringEntity.isPresent()) {
                mentoringLogGetIdDto = mentoringLogService.findAllMentoringLog(mentoringEntity.get().getMentoring_no());
                userEntity = userRepository.findByEmail(mentoringEntity.get().getApplicant()).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다." + mentoringEntity.get().getApplicant()));
            }
            UserMentoringApplyDetailDTO mentee = new UserMentoringApplyDetailDTO();
            mentee.setEmail(userEntity.getEmail());
            mentee.setAge(userEntity.getAge());
            mentee.setName(userEntity.getName());

            if(mentoringEntity.isPresent())
            mentoringLogAllLists.add(new MentoringLogAllListDto(mentoringEntity.get().getMentoring_no(),mentoringEntity.get().getM_no().getTitle(), mentee, mentoringLogGetIdDto));
        }

        return mentoringLogAllLists;
    }


    @ApiOperation(value = "로그 글 수정")
    @PatchMapping(value = "/mentoring/log/{log_no}")
    public ResultResponse updateMentoringLog(@PathVariable Long log_no, @RequestBody MentoringLogUpdateDto updateDto) {
        mentoringLogService.updateMentoringLog(log_no, updateDto);

        return new ResultResponse();
    }

    @ApiOperation(value = "로그 글 삭제")
    @DeleteMapping(value = "/mentoring/log/{log_no}")
    public ResultResponse deleteMentoringLog(@PathVariable Long log_no) {
        MentoringLogEntity mentoringLogEntity = mentoringLogService.findByMentoringLogId(log_no);
        mentoringLogService.deleteMentoringLog(mentoringLogEntity);

        return new ResultResponse();
    }
}
