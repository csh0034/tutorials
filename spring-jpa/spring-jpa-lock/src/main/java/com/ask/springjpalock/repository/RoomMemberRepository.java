package com.ask.springjpalock.repository;

import com.ask.springjpalock.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomMemberRepository extends JpaRepository<RoomMember, String> {

}
