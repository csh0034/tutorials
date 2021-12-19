package com.ask.springjpaquerydsl.repository;

import com.ask.springjpaquerydsl.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsRepository extends JpaRepository<Statistics, String> {

}