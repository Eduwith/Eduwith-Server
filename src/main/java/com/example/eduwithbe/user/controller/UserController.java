package com.example.eduwithbe.user.controller;

import com.example.eduwithbe.mentoring.dto.MentoringScrapGetDto;
import com.example.eduwithbe.mentoring.dto.MentoringScrapMNoDto;
import com.example.eduwithbe.mentoring.dto.ResultResponse;
import com.example.eduwithbe.mentoring.repository.MentoringRecruitScrapRepository;
import com.example.eduwithbe.user.domain.UserAttendanceEntity;
import com.example.eduwithbe.user.dto.*;
import com.example.eduwithbe.user.repository.UserAttendRepository;
import com.example.eduwithbe.user.service.UserAttendService;
import com.example.eduwithbe.user.service.UserService;
import com.example.eduwithbe.user.domain.UserEntity;
import com.example.eduwithbe.user.repository.UserRepository;
import com.example.eduwithbe.security.JwtTokenProvider;
import com.example.eduwithbe.security.Token;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Api(tags = {"UserController"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserAttendRepository userAttendRepository;
    private final UserService us;
    private final UserAttendService uas;
    private final MentoringRecruitScrapRepository scrapRepository;

    @ApiOperation(value = "회원가입")
    @PostMapping("/join")
    public @ResponseBody Map<String, Object> join(@RequestBody UserSaveDTO user) {
        user.setPwd(passwordEncoder.encode(user.getPwd()));
        userRepository.save(UserEntity.builder()
                .email(user.getEmail())
                .name(user.getName())
                .gender(user.getGender())
                .age(user.getAge())
                .address(user.getAddress())
                .pwd(passwordEncoder.encode(user.getPwd()))
                .roles(Collections.singletonList("ROLE_USER"))
                .build());

        Map<String, Object> response;
        response = us.save(user);
        return response;
    }

    @ApiOperation(value = "이메일 중복 체크")
    @PostMapping("/join/check")
    public ResultResponse joinCheck(@RequestBody UserJoinCheckDto userJoinCheckDto) {
        ResultResponse resultResponse = new ResultResponse();

        if(us.existsByEmail(userJoinCheckDto.getEmail())) resultResponse.setResult("FAILURE");
        else resultResponse.setResult("SUCCESS");

        return resultResponse;
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public Token login(@RequestBody @Validated UserLoginDTO user) {
//        log.info("user email = {}", user.get("userEmail"));
        //UserEntity member = userRepository.findByEmail(user.getEmail())
        UserEntity userEntity = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(user.getPwd(), userEntity.getPwd())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        return jwtTokenProvider.createAccessToken(userEntity.getUsername(), userEntity.getName(), userEntity.getRoles());
    }

    @ApiOperation(value = "로그인 시 토큰 체크")
    @PostMapping("/loginCheck")
    public Map<String, String> check(HttpServletRequest request) {
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));

        Optional<UserEntity> userEntity = userRepository.findByEmail(user);

        Map<String, String> map = new ManagedMap<>();
        map.put("email", user);

        userEntity.ifPresent(entity -> map.put("name", entity.getName()));
        return map;
    }

    @ApiOperation(value = "출석 체크 조회")
    @GetMapping("/attendance")
    public UserGetAttendDto getUserPoint(HttpServletRequest request){
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        UserEntity userEntity = userRepository.findByEmail(user).orElseThrow(() -> new IllegalArgumentException("신청 실패: 해당 유저가 존재하지 않습니다." + user));

        int stamp = userEntity.getStamp();
        int point = 100*(stamp/7);
        int day = userEntity.getDay();

        List<UserAttendanceEntity> userAttendanceEntities = userAttendRepository.findByEmailAttendance(user);
        return new UserGetAttendDto(stamp, point, day, userAttendanceEntities);
    }

    @ApiOperation(value = "출석 체크 반영")
    @PatchMapping("/attendance")
    public UserUpdateAttendanceDto updateUserPoint(HttpServletRequest request){
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        UserEntity userEntity = userRepository.findByEmail(user).orElseThrow(() -> new IllegalArgumentException("신청 실패: 해당 유저가 존재하지 않습니다." + user));

        int stamp = userEntity.getStamp()+1;
        int point = 100*(stamp/7);
        int day = userEntity.getDay()+1;

        if(day == 7) {
            uas.saveUserAttendApply(userEntity, "출석체크 리워드", 100);
            day = 0;
        }

        us.updateUserPoint(user, stamp, point, day);
        return new UserUpdateAttendanceDto(stamp, point, day);
    }

    @ApiOperation(value = "회원 수정")
    @PatchMapping(value = "/edit")
    public ResultResponse updateMentoringRecruit(HttpServletRequest request, @RequestBody UserUpdateDto updateDto) {
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        UserEntity userEntity = userRepository.findByEmail(user)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        ResultResponse resultResponse = new ResultResponse();
        if(!passwordEncoder.matches(updateDto.getPwd(), userEntity.getPassword())) {
            resultResponse.setResult("FAILURE"); //잘못된 비밀번호입니다.
        }
        else {
            resultResponse.setResult("SUCCESS");
            updateDto.setPwd(passwordEncoder.encode(updateDto.getChangePwd()));
            us.updateUser(user, updateDto);
        }

        return new ResultResponse();
    }

    @ApiOperation(value = "멘토링 모집글 스크랩")
    @PostMapping(value = "/scrap/mentoring")
    public ResultResponse saveMentoringRecruit(HttpServletRequest request, @RequestBody MentoringScrapMNoDto m_no) {
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        us.saveMentoringRecruitScrap(user, m_no.getM_no());

        return new ResultResponse();
    }

    @ApiOperation(value = "멘토링 모집글 스크랩 취소")
    @DeleteMapping(value = "/scrap/mentoring/{m_no}")
    public ResultResponse delMentoringRecruit(HttpServletRequest request, @PathVariable Long m_no) {
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));

        return new ResultResponse(us.deleteMentoringScrap(user, m_no));
    }

    @ApiOperation(value = "멘토링 모집글 스크랩 리스트")
    @GetMapping(value = "/scrap/mentoring")
    public List<MentoringScrapGetDto> getMentoringRecruit(HttpServletRequest request) {
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        return us.findByEmailScrap(user);
    }

    @ApiOperation(value = "회원 탈퇴")
    @DeleteMapping(value = "/withdrawal")
    public ResultResponse saveMentoringApplyRefuse(HttpServletRequest request) {
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        UserEntity userEntity = userRepository.findByEmail(user).orElseThrow(() -> new IllegalArgumentException("신청 실패: 해당 유저가 존재하지 않습니다." + user));

        userRepository.delete(userEntity);
        return new ResultResponse();
    }

    @ApiOperation(value = "프로필정보 보기")
    @GetMapping(value = "/mypage")
    public UserInfoDto getMyPage(HttpServletRequest request) {
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        UserEntity userEntity = userRepository.findByEmail(user).orElseThrow(() -> new IllegalArgumentException("신청 실패: 해당 유저가 존재하지 않습니다." + user));

        return new UserInfoDto(userEntity);
    }

}
