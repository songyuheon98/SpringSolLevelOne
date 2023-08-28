package com.hanghae.springsollevel1.entity;

import com.hanghae.springsollevel1.dto.LevelOneDataRequestDto;
import com.hanghae.springsollevel1.dto.LevelOneDataRequestPullDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Table(name="levelonedata")
@Getter
@Setter
@NoArgsConstructor
public class LevelOneData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="title",nullable = false)
    private String title;
    @Column(name="author",nullable = false)
    private String author;
    @Column(name="pw",nullable = false)
    private String pw;
    @Column(name="contents",nullable = false)
    private String contents;
    @Column(name = "nowTime",nullable = false)
    private String nowTime;


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

    public void update(LevelOneDataRequestPullDto levelOneDataResponsePullDto) {
        this.title = levelOneDataResponsePullDto.getTitle();
        this.author = levelOneDataResponsePullDto.getAuthor();
        this.pw = levelOneDataResponsePullDto.getPw();
        this.contents = levelOneDataResponsePullDto.getContents();
    }
}
