package com.hanghae.springsollevel1.controller;

import com.hanghae.springsollevel1.dto.LevelOneDataResponseDto;
import com.hanghae.springsollevel1.dto.LevelOneDataRequestDto;
import com.hanghae.springsollevel1.dto.LevelOneDataResponsePullDto;
import com.hanghae.springsollevel1.dto.LevelOneDataResponseSolTwoDto;
import com.hanghae.springsollevel1.entity.LevelOneData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@RestController
@RequestMapping("/api")
public class MainController {

    private final JdbcTemplate jdbcTemplate;
    private int i=0;
    public MainController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/contentsCreate") // 생성
    public LevelOneDataResponseDto createData(@RequestBody LevelOneDataRequestDto requestDto) {
        // RequestDto -> Entity
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        LevelOneData levelOneData = new LevelOneData(requestDto);

        Calendar cal = Calendar.getInstance();

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String nowTime = formatter.format(cal.getTime());

        levelOneData.setNowTime(nowTime);

//        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체
//
        String sql = "INSERT INTO levelOneData (title,author,pw, contents,nowTime) VALUES (?, ?,?,?,?)";
        // Sql
        jdbcTemplate.update( con -> {
            // update함수 DB의 레코드를 추가, 수정, 삭제 등의 작업을 할 때 사용
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);
                    // PreparedStatement = Java 타입을 MySQL DB 타입으로 자동 변환하기 위해 사용 ㅇㅅㅇ ...

                    preparedStatement.setString(1, levelOneData.getTitle());
                    preparedStatement.setString(2, levelOneData.getAuthor());
                    preparedStatement.setString(3, levelOneData.getPw());
                    preparedStatement.setString(4, levelOneData.getContents());
                    preparedStatement.setString(5, levelOneData.getNowTime());

                    return preparedStatement;
                },
                keyHolder);
//
        // DB Insert 후 받아온 기본키 확인
        Long key = keyHolder.getKey().longValue();
        levelOneData.setId(key);
//
//        // Entity -> ResponseDto
        LevelOneDataResponseDto levelOneDataResponseDto = new LevelOneDataResponseDto(levelOneData);
        return levelOneDataResponseDto;
    }

    @GetMapping("/allRead") // 모두 조회
    public List<LevelOneDataResponseSolTwoDto> getAllData() {
        // DB 조회
        String sql = "SELECT * FROM levelonedata";

        ArrayList<LevelOneDataResponseDto> tempLevelOneData = (ArrayList<LevelOneDataResponseDto>)
                jdbcTemplate.query(sql, new RowMapper<LevelOneDataResponseDto>() {
                    // return type = Array List
            @Override
            public LevelOneDataResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                // mapRow를 오버라이딩해서 사용자 정의 함수로 사영
                // 파라 미터 없어서 무조건 순서대로 해야함
                return new LevelOneDataResponseDto(
                        new LevelOneData(
                                rs.getLong("id"),
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getString("pw"),
                                rs.getString("contents"),
                                rs.getString("nowTime")
                        )
                );
            }
        });

        return tempLevelOneData.stream().sorted((a1,a2)->a2.getNowTime().compareTo(a1.getNowTime()))
                .map(f->new LevelOneDataResponseSolTwoDto(f.getTitle(),f.getAuthor(),f.getContents(),f.getNowTime()))
                .collect(Collectors.toList());
    }

    @GetMapping("/choiceRead/{title}") // 특정 조회
    public List<LevelOneDataResponseSolTwoDto> getChoiceData(@PathVariable String title){
        return getAllData().stream().filter(a->a.getTitle().equals(title)).collect(Collectors.toList());
    }


    @PutMapping("/choiceUpdate/{id}") // 선택 수정
    public List<LevelOneDataResponseSolTwoDto> updateMemo(@PathVariable Long id, @RequestBody LevelOneDataResponsePullDto levelOneDataResponsePullDto) {
        // 해당 메모가 DB에 존재하는지 확인
        LevelOneData levelOneData = findDataById(id);
        if(levelOneData != null) {
            if(levelOneDataResponsePullDto.getPw().equals(levelOneData.getPw())) {
                String sql = "UPDATE levelonedata SET title = ?, author =?, contents = ? WHERE id = ?";
                jdbcTemplate.update(sql,
                        levelOneDataResponsePullDto.getTitle(), levelOneDataResponsePullDto.getAuthor(),
                        levelOneDataResponsePullDto.getContents(),
                        id);
                return getAllData().stream().filter(a->a.getNowTime().equals(levelOneData.getNowTime())).collect(Collectors.toList());
            }
            else
                throw new IllegalArgumentException("PW 가 맞지 않습니다.");
        } else
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");

    }

    @DeleteMapping("/choiceDelete/{id}")
    public String updateMemo(@PathVariable Long id, @RequestBody Map<String,String> pw) {
        System.out.println(pw.get("pw"));
        // 해당 메모가 DB에 존재하는지 확인
        LevelOneData levelOneData = findDataById(id);
        if(levelOneData != null) {
            if(levelOneData.getPw().equals(pw.get("pw"))) {
                String sql = "DELETE FROM levelonedata WHERE id = ?";
                jdbcTemplate.update(sql, id);
                return "삭제 완료 됬어요";
            }
            else
                throw new IllegalArgumentException("PW 가 맞지 않습니다.");
        } else
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");

    }
    private LevelOneData findDataById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM levelonedata WHERE id = ?";

        return jdbcTemplate.query(sql, rs -> {
            if(rs.next()) {
                //찾은ㄱ
                LevelOneData levelOneData = new LevelOneData();

                levelOneData.setId(rs.getLong("id"));
                levelOneData.setTitle(rs.getString("title"));
                levelOneData.setAuthor(rs.getString("author"));
                levelOneData.setPw(rs.getString("pw"));
                levelOneData.setContents(rs.getString("contents"));
                levelOneData.setNowTime(rs.getString("nowTime"));

                return levelOneData;
            } else {
                return null;
            }
        }, id);
        // 마지막 인자 sql에 들어가는
    }
}
