package com.ask.springjpacore.repository;

import com.ask.springjpacore.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, String> {

}
