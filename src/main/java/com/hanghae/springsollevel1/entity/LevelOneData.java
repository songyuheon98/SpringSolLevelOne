package com.hanghae.springsollevel1.entity;

import com.hanghae.springsollevel1.dto.LevelOneDataRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LevelOneData {
    private String title;
    private String author;
    private String pw;
    private String contents;
    private String nowTime;
    private Long id;

    public LevelOneData( Long id, String title,String author, String pw, String contents, String nowTime) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.pw = pw;
        this.contents = contents;
        this.nowTime = nowTime;

    }

    public LevelOneData(LevelOneDataRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.author=requestDto.getAuthor();
        this.pw=requestDto.getPw();
        this.contents=requestDto.getContents();
    }



//    public void update(LevelOneDataRequestDto requestDto) {
//        this.title = requestDto.getTitle();
//        this.author=requestDto.getAuthor();
//        this.pw=requestDto.getPw();
//        this.contents=requestDto.getContents();
//
//    }
}
