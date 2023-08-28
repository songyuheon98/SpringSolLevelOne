package com.hanghae.springsollevel1.controller;

import com.hanghae.springsollevel1.dto.LevelOneDataRequestDto;
import com.hanghae.springsollevel1.dto.LevelOneDataResponseDto;
import com.hanghae.springsollevel1.dto.LevelOneDataRequestPullDto;
import com.hanghae.springsollevel1.dto.LevelOneDataResponseSolTwoDto;
import com.hanghae.springsollevel1.service.MainService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MainController {
    private final MainService mainService;
    public MainController(MainService mainService) {
        this.mainService = mainService;
    }
    @PostMapping("/data") // 생성
    public LevelOneDataResponseDto createData(@RequestBody LevelOneDataRequestDto requestDto) {
        return mainService.createData(requestDto);
    }

    @GetMapping("/data") // 모두 조회
    public List<LevelOneDataResponseSolTwoDto> getAllData() {
        return mainService.getAllData();
    }

    @GetMapping("/data/{id}") // 특정 조회
    public LevelOneDataResponseSolTwoDto getChoiceData(@PathVariable long id){
        return mainService.getChoiceData(id);
    }


    @PutMapping("/data/{id}") // 선택 수정
    public LevelOneDataResponseSolTwoDto updateData(@PathVariable Long id, @RequestBody LevelOneDataRequestPullDto levelOneDataResponsePullDto) {
        return mainService.updateData(id,levelOneDataResponsePullDto);
    }
    @DeleteMapping("/data/{id}")
    public String deleteData(@PathVariable Long id, @RequestBody Map<String,String> pw) {
        return mainService.deleteData(id,pw);
    }
}
