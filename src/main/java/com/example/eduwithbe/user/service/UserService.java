package com.example.eduwithbe.user.service;

import com.example.eduwithbe.mentoring.domain.MentoringApplyEntity;
import com.example.eduwithbe.mentoring.domain.MentoringRecruitmentEntity;
import com.example.eduwithbe.mentoring.domain.MentoringScrapEntity;
import com.example.eduwithbe.mentoring.dto.MentoringApplyEmailDto;
import com.example.eduwithbe.mentoring.dto.MentoringScrapGetDto;
import com.example.eduwithbe.mentoring.dto.MentoringScrapSaveDto;
import com.example.eduwithbe.mentoring.repository.MentoringRecruitScrapRepository;
import com.example.eduwithbe.mentoring.repository.MentoringRecruitmentRepository;
import com.example.eduwithbe.user.domain.UserEntity;
import com.example.eduwithbe.user.dto.UserSaveDTO;
import com.example.eduwithbe.user.dto.UserUpdateDto;
import com.example.eduwithbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    @Autowired
    private final UserRepository ur;

    @Autowired
    private final MentoringRecruitmentRepository mrr;

    @Autowired
    private final MentoringRecruitScrapRepository msr;

    SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:sss");
    Date time = new Date();
    String localTime = format.format(time);

    //유저 생성
    public Map<String, Object> save(UserSaveDTO userSaveDTO) {
        Map<String, Object> response = new HashMap<>();

        UserEntity userEntity = UserEntity.saveUser(userSaveDTO);
        userEntity.setStar(0F);

        if (userEntity.getEmail() != null & userEntity.getPwd() != null
                & userEntity.getAge() != 0 & userEntity.getName() != null
                & (userEntity.getGender() == 'M' || userEntity.getGender() == 'F')) {
            ur.save(userEntity);
            response.put("result", "SUCCESS");
            response.put("user", userEntity);
        } else {
            response.put("result", "FAIL");
            response.put("reason", "회원 가입 실패");
        }
        return response;
    }

    //유저 중복 이메일 확인
    public boolean existsByEmail(String email){
        return ur.existsByEmail(email);
    }

    //유저 정보 수정
    public void updateUser(String email, UserUpdateDto userUpdateDto) {
        ur.updateByUser(email, userUpdateDto.getName(), userUpdateDto.getPwd(), userUpdateDto.getAddress());
    }

    //유저 출석체크
    public void updateUserPoint(String email, int stamp, int point, int day) {
        ur.updateByUserPoint(email, stamp, point, day);
    }


    //유저 멘토링 스크랩
    public String saveMentoringRecruitScrap(String email, Long m_no) {
        UserEntity userEntity = ur.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다." + email));
        MentoringRecruitmentEntity mentoringRecruitment = mrr.findById(m_no).orElseThrow(() -> new IllegalArgumentException("해당 멘토링이 존재하지 않습니다." + m_no));

        MentoringScrapSaveDto mentoringScrapSaveDto = new MentoringScrapSaveDto();

        mentoringScrapSaveDto.setUser(userEntity);
        mentoringScrapSaveDto.setM_no(mentoringRecruitment);

        MentoringScrapEntity scrap = mentoringScrapSaveDto.toEntity();
        msr.save(scrap);

        //MentoringApplyEntity mentoringApply = mr.save(dto.toEntity());
        return "OK";
    }

    //유저 멘토링 스크랩 리스트
    public List<MentoringScrapGetDto> findByEmailScrap(String email) {

        List<MentoringScrapEntity> mentoringScrapEntity = msr.findByEmailMentoringScrap(email);

        return mentoringScrapEntity.stream()
                .map(MentoringScrapGetDto::new)
                .collect(Collectors.toList());
    }

    //유저 멘토링 스크랩 취소
    public String deleteMentoringScrap(String email, Long m_no) {
        MentoringScrapEntity mentoringScrapEntity = msr.findByEmailAndMNoMentoringScrap(email, m_no);
        msr.delete(mentoringScrapEntity);

        return "SUCCESS";
    }

    public UserEntity getUserFromAuth(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = userRepository.findByUserName(authentication.getName());
//        User user = userRepository.findByEmail(authentication.getName());
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return user;

    }

}
