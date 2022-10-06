package com.example.eduwithbe.mentoring.service;

import com.example.eduwithbe.mentoring.dto.*;
import com.example.eduwithbe.user.domain.UserEntity;
import com.example.eduwithbe.user.repository.UserRepository;
import com.example.eduwithbe.mentoring.domain.MentoringRecruitmentEntity;
import com.example.eduwithbe.mentoring.repository.MentoringRecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentoringRecruitmentServiceImpl implements MentoringRecruitmentService {

    @Autowired
    private final MentoringRecruitmentRepository mr;

    @Autowired
    private final UserRepository userRepository;

    //멘토링 작성 글 생성
    public void saveMentoringRecruit(String user, MentoringRecruitSaveDto dto){
        UserEntity userEntity = userRepository.findByEmail(user).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다." + user));
        MentoringRecruitSaveUserDto mentoringRecruitSaveUserDto = new MentoringRecruitSaveUserDto();
        mentoringRecruitSaveUserDto.setUser(userEntity);
        mentoringRecruitSaveUserDto.setField(dto.getField());
        mentoringRecruitSaveUserDto.setInfo(dto.getInfo());
        mentoringRecruitSaveUserDto.setRegion(dto.getRegion());
        mentoringRecruitSaveUserDto.setKeyword(dto.getKeyword());
        mentoringRecruitSaveUserDto.setRole(dto.getRole());
        mentoringRecruitSaveUserDto.setM_period(dto.getM_period());
        mentoringRecruitSaveUserDto.setTitle(dto.getTitle());
        mentoringRecruitSaveUserDto.setWay(dto.getWay());
        mentoringRecruitSaveUserDto.setName(userEntity.getName());

        MentoringRecruitmentEntity mentoringRecruit = mentoringRecruitSaveUserDto.toEntity();
        mr.save(mentoringRecruit);
    }

    //멘토링 작성 글 하나 가져옴
    public MentoringRecruitmentEntity findByMentoringRecruitId(Long m_no){
        return mr.findById(m_no).orElseThrow();
    }

    //멘토링 작성 글 전체 리스트
    public List<MentoringRecruitListDto> findAllMentoringRecruitment(){
        List<MentoringRecruitmentEntity> mentoringRecruitmentEntities = mr.findAll();
        return mentoringRecruitmentEntities.stream()
                .map(MentoringRecruitListDto::new)
                .collect(Collectors.toList());
    }

    //멘토링 작성 글 하나 수정
    public void updateMentoringRecruitment(Long m_no, MentoringRecruitUpdateDto updateDto) {
        mr.updateByMentoringRecruit(m_no, updateDto.getTitle(), updateDto.getRole(), updateDto.getField()
            ,updateDto.getRegion(), updateDto.getM_period(), updateDto.getWay(), updateDto.getKeyword(), updateDto.getInfo());
    }

    //멘토링 작성 글 하나 삭제
    public void deleteMentoringRecruit(MentoringRecruitmentEntity board){
        mr.delete(board);
    }

    //키워드 검색
    public List<MentoringRecruitListDto> findByTitleContaining(String keyword) {
        List<MentoringRecruitmentEntity> mentoringRecruitmentEntities =  mr.findByTitleContaining(keyword);
        return mentoringRecruitmentEntities.stream()
                .map(MentoringRecruitListDto::new)
                .collect(Collectors.toList());
    }

    //멘토링 멘토글 리스트
    public List<MentoringRecruitListDto> findByMentoringMentor() {

        List<MentoringRecruitmentEntity> mentoringRecruitmentEntities = mr.findByMentoringMentor();

        return mentoringRecruitmentEntities.stream()
                .map(MentoringRecruitListDto::new)
                .collect(Collectors.toList());
    }

    //멘토링 멘토글 리스트
    public List<MentoringRecruitListDto> findByMentoringMentee() {

        List<MentoringRecruitmentEntity> mentoringRecruitmentEntities = mr.findByMentoringMentee();

        return mentoringRecruitmentEntities.stream()
                .map(MentoringRecruitListDto::new)
                .collect(Collectors.toList());
    }

    //마이페이지 멘토링 글 목록 - 멘토
    public List<MentoringRecruitSearchDto> findByEmailMentoringMentor(String email) {

        List<MentoringRecruitmentEntity> mentoringRecruitmentEntities = mr.findByEmailMentoringMentor(email);

        return mentoringRecruitmentEntities.stream()
                .map(MentoringRecruitSearchDto::new)
                .collect(Collectors.toList());
    }

    //마이페이지 멘토링 글 목록 - 멘티
    public List<MentoringRecruitSearchDto> findByEmailMentoringMentee(String email) {

        List<MentoringRecruitmentEntity> mentoringRecruitmentEntities = mr.findByEmailMentoringMentee(email);

        return mentoringRecruitmentEntities.stream()
                .map(MentoringRecruitSearchDto::new)
                .collect(Collectors.toList());
    }

    public List<MentoringRecruitSearchDto> findByDistance(String email){
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다." + email));

        List<MentoringRecruitmentEntity> mentoringRecruitSearchDto = mr.findByDistance(userEntity.getEmail(), userEntity.getAddress());

        String str = userEntity.getAddress();
        String[] array = str.split(" ");

        List<MentoringRecruitmentEntity> mentoringRecruitSearchDto2 = mr.findByDistanceWide(userEntity.getEmail(), array[0], str);

        mentoringRecruitSearchDto.addAll(mentoringRecruitSearchDto2);

        return mentoringRecruitSearchDto.stream()
                .map(MentoringRecruitSearchDto::new)
                .collect(Collectors.toList());
    }

    //필터 검색
    public List<MentoringRecruitListDto> findByFilter(List<String> field, List<String> region, List<Integer> m_period, List<String> way) {

        List<MentoringRecruitmentEntity> mentoringRecruitmentEntities = mr.findByFilter(field, region, m_period, way);

        return mentoringRecruitmentEntities.stream()
                .map(MentoringRecruitListDto::new)
                .collect(Collectors.toList());
    }

}
