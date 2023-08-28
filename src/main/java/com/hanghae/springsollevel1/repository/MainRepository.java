package com.hanghae.springsollevel1.repository;

import com.hanghae.springsollevel1.entity.LevelOneData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainRepository extends JpaRepository<LevelOneData,Long> { 

}
