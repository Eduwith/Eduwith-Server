package com.example.eduwithbe.mentoring.controller;


import com.example.eduwithbe.mentoring.domain.MentoringApplyEntity;
import com.example.eduwithbe.mentoring.domain.MentoringRecruitmentEntity;
import com.example.eduwithbe.mentoring.dto.MentoringApplyEmailDto;
import com.example.eduwithbe.mentoring.dto.MentoringApplyUrgeDto;
import com.example.eduwithbe.mentoring.dto.ResultResponse;
import com.example.eduwithbe.mentoring.repository.MentoringApplyRepository;
import com.example.eduwithbe.mentoring.repository.MentoringRecruitmentRepository;
import com.example.eduwithbe.mentoring.service.MentoringApplyService;
import com.example.eduwithbe.mentoring.service.MentoringService;
import com.example.eduwithbe.notice.dto.NoticeSaveDto;
import com.example.eduwithbe.notice.service.NoticeService;
import com.example.eduwithbe.security.JwtTokenProvider;
import com.example.eduwithbe.user.domain.UserEntity;
import com.example.eduwithbe.user.dto.UserMentoringApplyDetailDTO;
import com.example.eduwithbe.user.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Api(tags = {"MentoringApplyController"})
@RequiredArgsConstructor
@RestController
public class MentoringApplyController {

    private final MentoringApplyService mentoringApplyService;
    private final MentoringService mentoringService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MentoringApplyRepository mentoringApplyRepository;
    private final MentoringRecruitmentRepository mentoringRecruitmentRepository;
    private final UserRepository userRepository;
    private final NoticeService noticeService;

    @ApiOperation(value = "멘토링 신청 리스트")
    @GetMapping("/mypage/apply")
    public List<MentoringApplyEmailDto> findByApplyEmail(HttpServletRequest request) {
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        return mentoringApplyService.findByEmail(user);
    }

    @ApiOperation(value = "멘토링 모집글 멘토멘티 지원")
    @PostMapping(value = "/mentoring/{m_no}/apply")
    public ResultResponse saveMentoringApply(HttpServletRequest request, @PathVariable Long m_no) {
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        String s = mentoringApplyService.saveMentoringApply(user, m_no);
        UserEntity userEntity = userRepository.findByEmail(user).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다." + user));

        ResultResponse resultResponse = new ResultResponse();

        if (Objects.equals(s, "OK")) {
            Optional<MentoringRecruitmentEntity> mentoringRecruitment = mentoringRecruitmentRepository.findById(m_no);

            String role = "";
            NoticeSaveDto noticeSaveDto = new NoticeSaveDto();
            if (mentoringRecruitment.isPresent())
                if (Objects.equals(mentoringRecruitment.get().getRole(), "O")) {
                    role = "멘토";
                } else role = "멘티";

            String finalRole = role;
            mentoringRecruitment.ifPresent(mentoringRecruitmentEntity -> noticeSaveDto.setTitle("[멘토링] " + mentoringRecruitmentEntity.getTitle()
                    + " - 방금 새로운 " + finalRole + " " + userEntity.getName() + "님이 지원했어요."));
            mentoringRecruitment.ifPresent(mentoringRecruitmentEntity -> noticeSaveDto.setUser(mentoringRecruitmentEntity.getUser()));
            noticeSaveDto.setField("Mentoring");
            noticeSaveDto.setRead("N");
            noticeService.saveNotice(noticeSaveDto);
            resultResponse.setResult("SUCCESS");
        } else if(Objects.equals(s, "NO")) resultResponse.setResult("FAILURE"); //Same email as author
        else resultResponse.setResult("FAILURE"); // Already supported
        return resultResponse;
    }

    @ApiOperation(value = "멘토링 모집 신청 수락")
    @PostMapping(value = "/mypage/{m_no}/apply/{apply_no}")
    public ResultResponse saveMentoringApplyAccept(HttpServletRequest request, @PathVariable Long m_no, @PathVariable Long apply_no) {
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        //MentoringApplyEntity mentoringApply = mentoringApplyRepository.findById(apply_no).orElseThrow(() -> new IllegalArgumentException("해당 신청이 존재하지 않습니다." + apply_no));

        String s = mentoringService.saveMentoring(user, m_no, apply_no);
        UserEntity userEntity = userRepository.findByEmail(user).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다." + user));

        if (Objects.equals(s, "OK")) {
            Optional<MentoringRecruitmentEntity> mentoringRecruitment = mentoringRecruitmentRepository.findById(m_no);
            NoticeSaveDto noticeSaveDto = new NoticeSaveDto();

            String finalRole = getRole(mentoringRecruitment);
            mentoringRecruitment.ifPresent(mentoringRecruitmentEntity -> noticeSaveDto.setTitle("[멘토링] " + mentoringRecruitmentEntity.getTitle() + " - " + finalRole + "신청이 수락되었어요."));
            noticeSaveDto.setUser(userEntity);
            noticeSaveDto.setField("Mentoring");
            noticeSaveDto.setRead("N");
            noticeService.saveNotice(noticeSaveDto);
        }
        return new ResultResponse();
    }

    @ApiOperation(value = "멘토링 모집 신청 거절")
    @DeleteMapping(value = "/mypage/{m_no}/apply/{apply_no}")
    public ResultResponse saveMentoringApplyRefuse(HttpServletRequest request, @PathVariable Long m_no, @PathVariable Long apply_no) {
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        String s = mentoringService.deleteMentoring(apply_no);
        UserEntity userEntity = userRepository.findByEmail(user).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다." + user));

        if (Objects.equals(s, "OK")) {
            Optional<MentoringRecruitmentEntity> mentoringRecruitment = mentoringRecruitmentRepository.findById(m_no);
            NoticeSaveDto noticeSaveDto = new NoticeSaveDto();

            String finalRole = getRole(mentoringRecruitment);
            mentoringRecruitment.ifPresent(mentoringRecruitmentEntity -> noticeSaveDto.setTitle(mentoringRecruitmentEntity.getTitle() + " 멘토링 - " + finalRole + "신청이 거절되었어요."));
            noticeSaveDto.setUser(userEntity);
            noticeSaveDto.setField("Mentoring");
            noticeSaveDto.setRead("N");
            noticeService.saveNotice(noticeSaveDto);
        }

        return new ResultResponse();
    }

    @ApiOperation(value = "멘토링 모집글 지원 수락 독촉")
    @PostMapping(value = "/mentoring/apply/urge")
    public ResultResponse saveMentoringUrgeNotice(HttpServletRequest request, @RequestBody MentoringApplyUrgeDto mentoringApplyUrgeDto) {
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        UserEntity userEntity = userRepository.findByEmail(user).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다." + user));
        Optional<MentoringRecruitmentEntity> mentoringRecruitment = mentoringRecruitmentRepository.findById(mentoringApplyUrgeDto.getM_no());
        MentoringApplyEntity mentoringApply = mentoringApplyRepository.findById(mentoringApplyUrgeDto.getApply_no()).orElseThrow(() -> new IllegalArgumentException("해당 멘토링 지원이 존재하지 않습니다." + mentoringApplyUrgeDto.getApply_no()));

        ResultResponse resultResponse = new ResultResponse();

        if (Objects.equals(user, mentoringApply.getEmail())) {
            if(mentoringRecruitment.isPresent()){
                String role = "";
                NoticeSaveDto noticeSaveDto = new NoticeSaveDto();
                if (Objects.equals(mentoringRecruitment.get().getRole(), "O")) {
                    role = "멘토";
                } else role = "멘티";

                String finalRole = role;
                mentoringRecruitment.ifPresent(mentoringRecruitmentEntity -> noticeSaveDto.setTitle("[멘토링] " + mentoringRecruitmentEntity.getTitle()
                        + " - " + finalRole + "로 지원하신 " + userEntity.getName() + "님이 지원에 대한 수락/거절을 요청하셨어요."));
                mentoringRecruitment.ifPresent(mentoringRecruitmentEntity -> noticeSaveDto.setUser(mentoringRecruitmentEntity.getUser()));
                noticeSaveDto.setField("Mentoring");
                noticeSaveDto.setRead("N");
                noticeService.saveNotice(noticeSaveDto);
                resultResponse.setResult("SUCCESS");
            } else resultResponse.setResult("FAILURE"); //Subscription does not exist
        } else resultResponse.setResult("FAILURE"); //Not the same email
        return resultResponse;
    }

    @ApiOperation(value = "멘토링 모집 신청 취소")
    @DeleteMapping(value = "/mypage/apply/{apply_no}")
    public ResultResponse saveMentoringApplyRefuse(@PathVariable Long apply_no) {
        mentoringService.deleteMentoring(apply_no);
        return new ResultResponse();
    }

    @ApiOperation(value = "멘토링 모집 신청한 회원 프로필 확인")
    @GetMapping(value = "/mypage/{apply_no}/profile")
    public UserMentoringApplyDetailDTO findOneApplyUser(@PathVariable Long apply_no) {
        Optional<MentoringApplyEntity> mentoringApply = mentoringApplyRepository.findById(apply_no);

        Optional<UserEntity> user = Optional.empty();
        if (mentoringApply.isPresent())
            user = userRepository.findByEmail(mentoringApply.get().getEmail());

        if (user.isPresent())
            return UserMentoringApplyDetailDTO.builder()
                    .email(user.get().getEmail())
                    .name(user.get().getName())
                    .age(user.get().getAge())
                    .build();
        else return UserMentoringApplyDetailDTO.builder()
                .email("이메일이 없습니다.")
                .name("이름이 없습니다.")
                .age(0)
                .build();
    }

    private String getRole(Optional<MentoringRecruitmentEntity> mentoringRecruitment) {
        String role = null;

        if (mentoringRecruitment.isPresent())
            if (Objects.equals(mentoringRecruitment.get().getRole(), "O")) {
                role = "멘토";
            } else role = "멘티";

        return role;
    }

}
