package com.example.eduwithbe.Volunteer.service;

import com.example.eduwithbe.Volunteer.Dto.VolunteerDto;
import com.example.eduwithbe.Volunteer.Dto.VolunteerScrapInfoDto;
import com.example.eduwithbe.Volunteer.domain.VolunteerScrapEntity;
import com.example.eduwithbe.Volunteer.repository.VolunteerRepository;
import com.example.eduwithbe.Volunteer.domain.VolunteerEntity;
import com.example.eduwithbe.Volunteer.repository.VolunteerScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VolunteerServiceImpl implements VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final VolunteerScrapRepository volunteerScrapRepository;

    // 전체 봉사 모집글 조회
    @Override
    public List<VolunteerDto> findVolunteers() {
        List<VolunteerEntity> volunteerEntities = volunteerRepository.findAll();
        return volunteerEntities.stream()
                .map(VolunteerDto::new)
                .collect(Collectors.toList());
    }

    // 특정 모집글 상세 보기
    @Override
    public VolunteerDto volunteerDetailView(Long no) {
        VolunteerEntity entity = volunteerRepository.findById(no)
                .orElseThrow(() -> new EntityNotFoundException("VolunteerDetailView : 해당 모집글이 존재하지 않습니다."));
        return new VolunteerDto(entity);
    }

    @Override
    // 자원봉사 스크랩
    public String saveVolunteerScrap(VolunteerScrapInfoDto vs, String myEmail) {
        VolunteerEntity entity = volunteerRepository.findById(vs.getV_no())
                .orElseThrow(() -> new IllegalArgumentException("saveVolunteerScrap : 해당 글이 존재하지 않습니다."));

        VolunteerScrapEntity scrap = VolunteerScrapEntity.builder()
                .email(myEmail)
                .v_no(vs.getV_no())
                .build();

        if(vs.getScrapYn() == 'Y') {    // 스크랩 버튼 누름
            volunteerScrapRepository.save(scrap);
        } else { // 스크랩 취소
            volunteerScrapRepository.deleteById(scrap.getScrapNo());
        }

        return "success";

    }
}
