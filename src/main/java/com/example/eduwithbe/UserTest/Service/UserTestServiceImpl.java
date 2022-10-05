package com.example.eduwithbe.UserTest.Service;

import com.example.eduwithbe.UserTest.DTO.UserTestSaveDto;
import com.example.eduwithbe.UserTest.Repository.UserTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserTestServiceImpl implements UserTestService {
    private final UserTestRepository userTestRepository;

    @Override
    // 테스트 결과 저장
    public String saveTestResult(String email, UserTestSaveDto utDto) {
        // mate-mbti 저장하기
        String myMbti = utDto.getMbti();
        String mate_mbti;
        switch (myMbti) {
            case "INTJ": case "ENTP" :
                if(myMbti.equals("INTJ")) mate_mbti = "ENTP";
                else mate_mbti = "INTJ";
                break;
            case "INTP": case "ENTJ" :
                if(myMbti.equals("INTP")) mate_mbti = "ENTJ";
                else mate_mbti = "INTP";
                break;
            case "INFJ": case "ENFP":
                if(myMbti.equals("INFJ")) mate_mbti = "ENFP";
                else mate_mbti = "INFJ";
                break;
            case "INFP": case "ENFJ":
                if(myMbti.equals("INFP")) mate_mbti = "ENFJ";
                else mate_mbti = "INFP";
                break;
            case "ISTJ": case "ESTP":
                if(myMbti.equals("ISTJ")) mate_mbti = "ESTP";
                else mate_mbti = "ISTJ";
                break;
            case "ISFJ": case "ESFP":
                if(myMbti.equals("ISFJ")) mate_mbti = "ESFP";
                else mate_mbti = "ISFJ";
                break;
            case "ISTP": case "ESTJ":
                if(myMbti.equals("ISTP")) mate_mbti = "ESTJ";
                else mate_mbti = "ISTP";
                break;
            case "ISFP": case "ESFJ":
                if(myMbti.equals("ISFP")) mate_mbti = "ESFJ";
                else mate_mbti = "ISFP";
                break;
            default:
                return "해당되는 MBTI가 없습니다.";

        }
        System.out.println("========= mate mbti : " + mate_mbti);

        userTestRepository.save(utDto.toEntity(email, mate_mbti));

        return "success";
    }

}
