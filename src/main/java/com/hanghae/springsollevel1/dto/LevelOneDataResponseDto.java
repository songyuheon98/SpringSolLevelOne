package com.hanghae.springsollevel1.dto;

import com.hanghae.springsollevel1.entity.LevelOneData;
import lombok.Getter;

@Getter
public class LevelOneDataResponseDto {
    private String title;
    private String author;
    private String pw;
    private String contents;
    private String nowTime;
    private Long id;

    public LevelOneDataResponseDto(LevelOneData levelOneData) {
        this.title = levelOneData.getTitle();
        this.author=levelOneData.getAuthor();
        this.pw=levelOneData.getPw();
        this.contents=levelOneData.getContents();
        this.id=levelOneData.getId();
        this.nowTime=levelOneData.getNowTime();
    }
}
