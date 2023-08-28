package com.hanghae.springsollevel1.controller;

import com.hanghae.springsollevel1.dto.LevelOneDataRequestDto;
import com.hanghae.springsollevel1.dto.LevelOneDataResponseDto;
import com.hanghae.springsollevel1.dto.LevelOneDataResponsePullDto;
import com.hanghae.springsollevel1.dto.LevelOneDataResponseSolTwoDto;
import com.hanghae.springsollevel1.service.MainService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MainController {
    private final JdbcTemplate jdbcTemplate;
    public MainController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/data") // 생성
    public LevelOneDataResponseDto createData(@RequestBody LevelOneDataRequestDto requestDto) {
        MainService mainService = new MainService(jdbcTemplate);
        return new LevelOneDataResponseDto(mainService.createData(requestDto));
    }

    @GetMapping("/data") // 모두 조회
    public List<LevelOneDataResponseSolTwoDto> getAllData() {
        MainService mainService = new MainService(jdbcTemplate);
        return mainService.getAllData();
    }

    @GetMapping("/data/{id}") // 특정 조회
    public List<LevelOneDataResponseSolTwoDto> getChoiceData(@PathVariable long id){
        MainService mainService = new MainService(jdbcTemplate);
        return mainService.getChoiceData(id);
    }


    @PutMapping("/data/{id}") // 선택 수정
    public List<LevelOneDataResponseSolTwoDto> updateData(@PathVariable Long id, @RequestBody LevelOneDataResponsePullDto levelOneDataResponsePullDto) {
        MainService mainService = new MainService(jdbcTemplate);
        return mainService.updateData(id,levelOneDataResponsePullDto);
    }
    @DeleteMapping("/data/{id}")
    public String deleteData(@PathVariable Long id, @RequestBody Map<String,String> pw) {
        MainService mainService = new MainService(jdbcTemplate);
        return mainService.deleteData(id,pw);
    }
}
