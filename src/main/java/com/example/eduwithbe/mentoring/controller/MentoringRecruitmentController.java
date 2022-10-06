package com.example.eduwithbe.mentoring.controller;

import com.example.eduwithbe.mentoring.domain.MentoringScrapEntity;
import com.example.eduwithbe.mentoring.dto.*;
import com.example.eduwithbe.mentoring.repository.MentoringRecruitScrapRepository;
import com.example.eduwithbe.mentoring.service.MentoringRecruitmentService;
import com.example.eduwithbe.mentoring.domain.MentoringRecruitmentEntity;
import com.example.eduwithbe.security.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Api(tags = {"MentoringRecruitController"})
@RequestMapping(value = "/mentoring")
@RequiredArgsConstructor
@RestController
public class MentoringRecruitmentController {

    private final MentoringRecruitmentService mentoringService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MentoringRecruitScrapRepository mentoringRecruitScrapRepository;

    @ApiOperation(value = "멘토링 모집글 작성")
    @PostMapping(value = "/recruitment")
    public ResultResponse saveMentoringRecruit(HttpServletRequest request, @RequestBody MentoringRecruitSaveDto saveBoardDto) {
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        mentoringService.saveMentoringRecruit(user, saveBoardDto);

        return new ResultResponse();
    }

    @ApiOperation(value = "멘토링 모집글 상세 조회")
    @GetMapping(value = "/{m_no}")
    public MentoringRecruitScrapDto findOneMentoringRecruit(HttpServletRequest request, @PathVariable Long m_no) {
        MentoringRecruitmentEntity mentoringRecruitment = mentoringService.findByMentoringRecruitId(m_no);
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        List<MentoringScrapEntity> scrapEntity = mentoringRecruitScrapRepository.findByEmailAndMNoMentoringScrapList(user, m_no);
        String scrap = "N";
        if(scrapEntity.size() > 0) scrap = "Y";

        return MentoringRecruitScrapDto.builder().me(mentoringRecruitment).scrap(scrap).build();
    }

    @ApiOperation(value = "멘토링 모집글 전체 리스트")
    @GetMapping(value = "/list")
    public List<MentoringRecruitListDto> findAllMentoring() {
        return mentoringService.findAllMentoringRecruitment();
    }

    @ApiOperation(value = "멘토링 모집글 멘토 리스트")
    @GetMapping(value = "/mentor")
    public List<MentoringRecruitListDto> findAllMentoringMentor() {
        return mentoringService.findByMentoringMentor();
    }

    @ApiOperation(value = "멘토링 모집글 멘티 리스트")
    @GetMapping(value = "/mentee")
    public List<MentoringRecruitListDto> findAllMentoringMentee() {
        return mentoringService.findByMentoringMentee();
    }

    @ApiOperation(value = "멘토링 모집글 수정")
    @PatchMapping(value = "/{m_no}")
    public ResultResponse updateMentoringRecruit(@PathVariable Long m_no, @RequestBody MentoringRecruitUpdateDto updateDto) {
        mentoringService.updateMentoringRecruitment(m_no, updateDto);

        return new ResultResponse();
    }

    @ApiOperation(value = "멘토링 모집글 삭제")
    @DeleteMapping(value = "/{m_no}")
    public ResultResponse deleteBoard(@PathVariable Long m_no) {
        MentoringRecruitmentEntity mentoringRecruitment = mentoringService.findByMentoringRecruitId(m_no);
        mentoringService.deleteMentoringRecruit(mentoringRecruitment);

        return new ResultResponse();
    }

    @ApiOperation(value = "멘토링 모집글 키워드 검색")
    @GetMapping("/search/{keyword}")
    public List<MentoringRecruitListDto> findByTitleContaining(@PathVariable String keyword) {
        return mentoringService.findByTitleContaining(keyword);
    }


    @ApiOperation(value = "마이페이지 모집글 리스트")
    @GetMapping("/mypage/mentoring")
    public MentoringMentorMenteeDto findByMentorAndMentee(HttpServletRequest request){
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        List<MentoringRecruitSearchDto> mentor = mentoringService.findByEmailMentoringMentor(user);
        List<MentoringRecruitSearchDto> mentee = mentoringService.findByEmailMentoringMentee(user);

        return MentoringMentorMenteeDto.builder().mentor(mentor).mentee(mentee).build();
    }

    @ApiOperation(value = "메인화면 : 멘토링 모집글 지역순")
    @GetMapping("/distance")
    public List<MentoringRecruitSearchDto> findByDistance(HttpServletRequest request){
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        return mentoringService.findByDistance(user);
    }

    @ApiOperation(value = "멘토링 모집글 필터 검색")
    @GetMapping("/search/filter")
    public List<MentoringRecruitListDto> searchByFilter(@RequestParam(required = false, defaultValue = "") String field, @RequestParam(required = false, defaultValue = "") String region, @RequestParam(required = false, defaultValue = "0") int m_period, @RequestParam(required = false, defaultValue = "") String way){
        List<String> fieldList;
        if(Objects.equals(field, "")){
            fieldList = List.of("진로", "교육", "문화예술스포츠", "기타");
        }else{
            fieldList = List.of(field);
        }

        List<String> regionList;
        if(Objects.equals(region, "")){
            regionList = List.of("서울특별시 강남구", "서울특별시 강동구", "서울특별시 강북구", "서울특별시 강서구", "서울특별시 관악구", "서울특별시 광진구"
            , "서울특별시 구로구", "서울특별시 금천구", "서울특별시 노원구", "서울특별시 도봉구", "서울특별시 동대문구", "서울특별시 동작구", "서울특별시 마포구"
            , "서울특별시 서대문구", "서울특별시 서초구", "서울특별시 성동구", "서울특별시 성북구", "서울특별시 송파구", "서울특별시 양천구", "서울특별시 영등포구"
            , "서울특별시 용산구", "서울특별시 은평구", "서울특별시 종로구", "서울특별시 중구", "서울특별시 중랑구"
            , "인천광역시 계양구","인천광역시 남구","인천광역시 남동구","인천광역시 동구","인천광역시 부평구","인천광역시 서구","인천광역시 연수구","인천광역시 중구","인천광역시 강화군","인천광역시 옹진군"
            , "대전광역시 대덕구","대전광역시 동구","대전광역시 서구","대전광역시 유성구","대전광역시 중구"
            , "광주광역시 광산구","광주광역시 남구","광주광역시 동구", "광주광역시 북구","광주광역시 서구"
            , "대구광역시 남구","대구광역시 달서구","대구광역시 동구","대구광역시 북구","대구광역시 서구","대구광역시 수성구","대구광역시 중구","대구광역시 달성군"
            , "울산광역시 남구","울산광역시 동구","울산광역시 북구","울산광역시 중구","울산광역시 울주군"
            , "부산광역시 강서구","부산광역시 금정구","부산광역시 남구","부산광역시 동구","부산광역시 동래구","부산광역시 부산진구","부산광역시 북구","부산광역시 사상구",
                    "부산광역시 사하구","부산광역시 서구","부산광역시 수영구","부산광역시 연제구","부산광역시 영도구","부산광역시 중구","부산광역시 해운대구","부산광역시 기장군"
            ,"경기도 고양시","경기도 과천시","경기도 광명시","경기도 광주시","경기도 구리시","경기도 군포시","경기도 김포시","경기도 남양주시","경기도 동두천시","경기도 부천시","경기도 성남시"
                    ,"경기도 수원시","경기도 시흥시","경기도 안산시","경기도 안성시","경기도 안양시","경기도 양주시","경기도 오산시","경기도 용인시","경기도 의왕시","경기도 의정부시","경기도 이천시",
                    "경기도 파주시","경기도 평택시","경기도 포천시","경기도 하남시","경기도 화성시","경기도 가평군","경기도 양평군","경기도 여주군","경기도 연천군"
            , "강원도 강릉시","강원도 동해시","강원도 삼척시","강원도 속초시","강원도 원주시","강원도 춘천시","강원도 태백시","강원도 고성군","강원도 양구군","강원도 양양군","강원도 영월군","강원도 인제군","강원도 정선군"
                    ,"강원도 철원군","강원도 평창군","강원도 홍천군","강원도 화천군","강원도 횡성군"
            , "충청북도 제천시","충청북도 청주시","충청북도 충주시","충청북도 괴산군","충청북도 단양군","충청북도 보은군","충청북도 영동군","충청북도 옥천군","충청북도 음성군","충청북도 증평군","충청북도 진천군", "충청북도 청원군"
            , "충청남도 계룡시","충청남도 공주시","충청남도 논산시","충청남도 보령시","충청남도 서산시","충청남도 아산시","충청남도 천안시","충청남도 금산군","충청남도 당진군","충청남도 부여군","충청남도 서천군", "충청남도 연기군",
                    "충청남도 예산군","충청남도 청양군","충청남도 태안군","충청남도 홍성군"
            , "전라북도 군산시","전라북도 김제시","전라북도 남원시","전라북도 익산시","전라북도 전주시","전라북도 정읍시","전라북도 고창군","전라북도 무주군","전라북도 부안군","전라북도 순창군","전라북도 완주군","전라북도 임실군"
                    ,"전라북도 장수군","전라북도 진안군"
            , "전라남도 광양시","전라남도 나주시","전라남도 목포시","전라남도 순천시","전라남도 여수시","전라남도 강진군","전라남도 고흥군","전라남도 곡성군","전라남도 구례군","전라남도 담양군","전라남도 무안군","전라남도 보성군",
                    "전라남도 신안군","전라남도 영광군","전라남도 영암군","전라남도 완도군","전라남도 장성군","전라남도 장흥군","전라남도 진도군","전라남도 함평군","전라남도 해남군","전라남도 화순군"
            , "경상북도 경산시","경상북도경주시","경상북도 구미시","경상북도 김천시","경상북도 문경시","경상북도 상주시","경상북도 안동시","경상북도 영주시","경상북도 영천시","경상북도 포항시","경상북도 고령군","경상북도 군위군",
                    "경상북도 봉화군","경상북도 성주군","경상북도 영덕군","경상북도 영양군","경상북도 예천군","경상북도 울릉군","경상북도 울진군","경상북도 의성군","경상북도 청도군","경상북도 청송군","경상북도 칠곡군"
            , "경상남도 거제시","경상남도 김해시","경상남도 마산시","경상남도 밀양시","경상남도 사천시","경상남도 양산시","경상남도 진주시","경상남도 진해시","경상남도 창원시","경상남도 통영시","경상남도 거창군","경상남도 고성군",
                    "경상남도 남해군","경상남도 산청군","경상남도 의령군","경상남도 창녕군","경상남도 하동군","경상남도 함안군","경상남도 함양군","경상남도 합천군"
            , "제주도 서귀포시","제주도 제주시","제주도 남제주군","제주도 북제주군");
        }else{
            regionList = List.of(region);
        }

        List<Integer> periodList;
        if(Objects.equals(m_period, 0)){
            periodList = List.of(1, 3, 6, 12);
        }else{
            periodList = List.of(m_period);
        }

        List<String> wayList;
        if(Objects.equals(way, "")){
            wayList = List.of("ON", "OFF");
        }else{
            wayList = List.of(way);
        }

        return mentoringService.findByFilter(fieldList, regionList, periodList, wayList);


    }

}
