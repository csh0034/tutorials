package com.ask.springjpalock.repository;

import com.ask.springjpalock.entity.RoomMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomMessageRepository extends JpaRepository<RoomMessage, String> {

}
