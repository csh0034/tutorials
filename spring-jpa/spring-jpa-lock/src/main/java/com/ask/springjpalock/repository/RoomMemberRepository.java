package com.ask.springjpalock.repository;

import com.ask.springjpalock.entity.RoomMember;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface RoomMemberRepository extends JpaRepository<RoomMember, String> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<RoomMember> findForUpdateById(String id);

}
