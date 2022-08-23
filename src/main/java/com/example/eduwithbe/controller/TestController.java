package com.example.eduwithbe.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"TestController"})
@RestController
public class TestController {

    @ApiOperation(value = "프론트 연결 테스트")
    @GetMapping("/test")
    public String test(){
        return "통과123123";
    }
}
