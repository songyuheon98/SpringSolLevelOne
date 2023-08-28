package com.hanghae.springsollevel1.service;

import com.hanghae.springsollevel1.dto.LevelOneDataRequestDto;
import com.hanghae.springsollevel1.dto.LevelOneDataResponsePullDto;
import com.hanghae.springsollevel1.dto.LevelOneDataResponseSolTwoDto;
import com.hanghae.springsollevel1.entity.LevelOneData;
import com.hanghae.springsollevel1.repository.MainRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MainService {
    private final MainRepository mainRepository;

    private LevelOneData levelOneData;
    public MainService(MainRepository mainRepository,LevelOneData levelOneData) {
        this.mainRepository = mainRepository;
    }

    public LevelOneData createData(LevelOneDataRequestDto requestDto) {
        this.levelOneData = new LevelOneData(requestDto);
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String nowTime = formatter.format(cal.getTime());

        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        levelOneData.setNowTime(nowTime);
        return mainRepository.save(levelOneData);
    }

    public List<LevelOneDataResponseSolTwoDto> getAllData() {
        return mainRepository.findAllData();

    }

    public List<LevelOneDataResponseSolTwoDto> getChoiceData(long id) {
        return getAllData().stream().filter(a->a.getNowTime().equals(mainRepository
                .findDataById(id).getNowTime())).collect(Collectors.toList());
    }

    public List<LevelOneDataResponseSolTwoDto> updateData(Long id, LevelOneDataResponsePullDto levelOneDataResponsePullDto) {
        // 해당 메모가 DB에 존재하는지 확인
        this.levelOneData = mainRepository.findDataById(id);

        if(levelOneData != null) {
            if(levelOneDataResponsePullDto.getPw().equals(levelOneData.getPw())) {
                mainRepository.update(id,levelOneDataResponsePullDto);
                return getAllData().stream().filter(a->a.getNowTime().equals(levelOneData.getNowTime())).collect(Collectors.toList());
            }
            else
                throw new IllegalArgumentException("PW 가 맞지 않습니다.");
        } else
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
    }




    public String deleteData(Long id, Map<String, String> pw) {
        this.levelOneData = mainRepository.findDataById(id);

        if(levelOneData != null) {
            if(levelOneData.getPw().equals(pw.get("pw"))) {
                mainRepository.delete(id);
                return "{\"success\":\"true\"}";
            }
            else
                throw new IllegalArgumentException("PW 가 맞지 않습니다.");
        } else
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
    }
}
