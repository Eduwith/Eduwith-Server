package com.example.eduwithbe.mentoring.controller;

import com.example.eduwithbe.mentoring.dto.*;
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

    @ApiOperation(value = "멘토링 모집글 작성")
    @PostMapping(value = "/recruitment")
    public ResultResponse saveMentoringRecruit(HttpServletRequest request, @RequestBody MentoringRecruitSaveDto saveBoardDto) {
        String user = jwtTokenProvider.getUserPk(request.getHeader("Authorization"));
        mentoringService.saveMentoringRecruit(user, saveBoardDto);

        return new ResultResponse();
    }

    @ApiOperation(value = "멘토링 모집글 상세 조회")
    @GetMapping(value = "/{m_no}")
    public MentoringRecruitDto findOneMentoringRecruit(@PathVariable Long m_no) {
        MentoringRecruitmentEntity mentoringRecruitment = mentoringService.findByMentoringRecruitId(m_no);

        return MentoringRecruitDto.builder().me(mentoringRecruitment).build();
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
            regionList = List.of("서울특별시강남구", "서울특별시강동구", "서울특별시강북구", "서울특별시강서구", "서울특별시관악구", "서울특별시광진구"
            , "서울특별시구로구", "서울특별시금천구", "서울특별시노원구", "서울특별시도봉구", "서울특별시동대문구", "서울특별시동작구", "서울특별시마포구"
            , "서울특별시서대문구", "서울특별시서초구", "서울특별시성동구", "서울특별시성북구", "서울특별시송파구", "서울특별시양천구", "서울특별시영등포구"
            , "서울특별시용산구", "서울특별시은평구", "서울특별시종로구", "서울특별시중구", "서울특별시중랑구"
            , "인천광역시계양구","인천광역시남구","인천광역시남동구","인천광역시동구","인천광역시부평구","인천광역시서구","인천광역시연수구","인천광역시중구","인천광역시강화군","인천광역시옹진군"
            , "대전광역시대덕구","대전광역시동구","대전광역시서구","대전광역시유성구","대전광역시중구"
            , "광주광역시광산구","광주광역시남구","광주광역시동구", "광주광역시북구","광주광역시서구"
            , "대구광역시남구","대구광역시달서구","대구광역시동구","대구광역시북구","대구광역시서구","대구광역시수성구","대구광역시중구","대구광역시달성군"
            , "울산광역시남구","울산광역시동구","울산광역시북구","울산광역시중구","울산광역시울주군"
            , "부산광역시강서구","부산광역시금정구","부산광역시남구","부산광역시동구","부산광역시동래구","부산광역시부산진구","부산광역시북구","부산광역시사상구",
                    "부산광역시사하구","부산광역시서구","부산광역시수영구","부산광역시연제구","부산광역시영도구","부산광역시중구","부산광역시해운대구","부산광역시기장군"
            ,"경기도고양시","경기도과천시","경기도광명시","경기도광주시","경기도구리시","경기도군포시","경기도김포시","경기도남양주시","경기도동두천시","경기도부천시","경기도성남시"
                    ,"경기도수원시","경기도시흥시","경기도안산시","경기도안성시","경기도안양시","경기도양주시","경기도오산시","경기도용인시","경기도의왕시","경기도의정부시","경기도이천시",
                    "경기도파주시","경기도평택시","경기도포천시","경기도하남시","경기도화성시","경기도가평군","경기도양평군","경기도여주군","경기도연천군"
            , "강원도강릉시","강원도동해시","강원도삼척시","강원도속초시","강원도원주시","강원도춘천시","강원도태백시","강원도고성군","강원도양구군","강원도양양군","강원도영월군","강원도인제군","강원도정선군"
                    ,"강원도철원군","강원도평창군","강원도홍천군","강원도화천군","강원도횡성군"
            , "충청북도제천시","충청북도청주시","충청북도충주시","충청북도괴산군","충청북도단양군","충청북도보은군","충청북도영동군","충청북도옥천군","충청북도음성군","충청북도증평군","충청북도진천군", "충청북도청원군"
            , "충청남도계룡시","충청남도공주시","충청남도논산시","충청남도보령시","충청남도서산시","충청남도아산시","충청남도천안시","충청남도금산군","충청남도당진군","충청남도부여군","충청남도서천군","충청남도연기군",
                    "충청남도예산군","충청남도청양군","충청남도태안군","충청남도홍성군"
            , "전라북도군산시","전라북도김제시","전라북도남원시","전라북도익산시","전라북도전주시","전라북도정읍시","전라북도고창군","전라북도무주군","전라북도부안군","전라북도순창군","전라북도완주군","전라북도임실군"
                    ,"전라북도장수군","전라북도진안군"
            , "전라남도광양시","전라남도나주시","전라남도목포시","전라남도순천시","전라남도여수시","전라남도강진군","전라남도고흥군","전라남도곡성군","전라남도구례군","전라남도담양군","전라남도무안군","전라남도보성군",
                    "전라남도신안군","전라남도영광군","전라남도영암군","전라남도완도군","전라남도장성군","전라남도장흥군","전라남도진도군","전라남도함평군","전라남도해남군","전라남도화순군"
            , "경상북도경산시","경상북도경주시","경상북도구미시","경상북도김천시","경상북도문경시","경상북도상주시","경상북도안동시","경상북도영주시","경상북도영천시","경상북도포항시","경상북도고령군","경상북도군위군",
                    "경상북도봉화군","경상북도성주군","경상북도영덕군","경상북도영양군","경상북도예천군","경상북도울릉군","경상북도울진군","경상북도의성군","경상북도청도군","경상북도청송군","경상북도칠곡군"
            , "경상남도거제시","경상남도김해시","경상남도마산시","경상남도밀양시","경상남도사천시","경상남도양산시","경상남도진주시","경상남도진해시","경상남도창원시","경상남도통영시","경상남도거창군","경상남도고성군",
                    "경상남도남해군","경상남도산청군","경상남도의령군","경상남도창녕군","경상남도하동군","경상남도함안군","경상남도함양군","경상남도합천군"
            , "제주도서귀포시","제주도제주시","제주도남제주군","제주도북제주군");
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
