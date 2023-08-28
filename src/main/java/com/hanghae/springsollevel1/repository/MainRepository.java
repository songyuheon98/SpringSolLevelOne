package com.hanghae.springsollevel1.repository;

import com.hanghae.springsollevel1.dto.LevelOneDataResponseDto;
import com.hanghae.springsollevel1.dto.LevelOneDataResponsePullDto;
import com.hanghae.springsollevel1.dto.LevelOneDataResponseSolTwoDto;
import com.hanghae.springsollevel1.entity.LevelOneData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MainRepository {
    private final JdbcTemplate jdbcTemplate;
    public MainRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public LevelOneData save(LevelOneData levelOneData) {
        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

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
        return levelOneData;
    }

    public List<LevelOneDataResponseSolTwoDto> findAllData() {
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



    public LevelOneData findDataById(Long id) {
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
            } else
                return null;

        }, id);
        // 마지막 인자 sql에 들어가는
    }

    public void update(Long id, LevelOneDataResponsePullDto levelOneDataResponsePullDto) {
        String sql = "UPDATE levelonedata SET title = ?, author =?, contents = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                levelOneDataResponsePullDto.getTitle(), levelOneDataResponsePullDto.getAuthor(),
                levelOneDataResponsePullDto.getContents(),
                id);

    }

    public void delete(Long id) {
        String sql = "DELETE FROM levelonedata WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
