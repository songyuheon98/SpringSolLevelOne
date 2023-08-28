package com.hanghae.springsollevel1.service;
import com.hanghae.springsollevel1.dto.LevelOneDataRequestDto;
import com.hanghae.springsollevel1.dto.LevelOneDataRequestPullDto;
import com.hanghae.springsollevel1.dto.LevelOneDataResponseDto;
import com.hanghae.springsollevel1.dto.LevelOneDataResponseSolTwoDto;
import com.hanghae.springsollevel1.entity.LevelOneData;
import com.hanghae.springsollevel1.repository.MainRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class MainService {
    private final MainRepository mainRepository;
    private LevelOneData levelOneData;
    public MainService(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }


    public LevelOneDataResponseDto createData(LevelOneDataRequestDto requestDto) {
        this.levelOneData = new LevelOneData(requestDto);
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String nowTime = formatter.format(cal.getTime());
        levelOneData.setNowTime(nowTime);

        return new LevelOneDataResponseDto(mainRepository.save(levelOneData));
    }

    public List<LevelOneDataResponseSolTwoDto> getAllData() {
        return mainRepository.findAll().stream().sorted((a1,a2)->a2.getNowTime().compareTo(a1.getNowTime()))
                .map(f->new LevelOneDataResponseSolTwoDto(f.getTitle(),f.getAuthor(),f.getContents(),f.getNowTime()))
                .collect(Collectors.toList());
    }

    public LevelOneDataResponseSolTwoDto getChoiceData(long id) {
        return new LevelOneDataResponseSolTwoDto(findData(id));
    }

    // getAllData
    @Transactional
    public LevelOneDataResponseSolTwoDto updateData(Long id, LevelOneDataRequestPullDto levelOneDataResponsePullDto) {
        // 해당 메모가 DB에 존재하는지 확인
        this.levelOneData = findData(id);
        this.levelOneData.update(levelOneDataResponsePullDto);
        return new LevelOneDataResponseSolTwoDto(this.levelOneData);
    }

    public String deleteData(Long id, Map<String, String> pw) {
        this.levelOneData = findData(id);
        if(this.levelOneData.getPw().equals(pw.get("pw"))) {
            mainRepository.delete(this.levelOneData);
            return "{\"success\":\"true\"}";
        }
        else
            throw new IllegalArgumentException("PW 가 맞지 않습니다.");
    }

    private LevelOneData findData(Long id){
        return mainRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("선택한 데이터는 존재하지 않습니다."));
    }
}
