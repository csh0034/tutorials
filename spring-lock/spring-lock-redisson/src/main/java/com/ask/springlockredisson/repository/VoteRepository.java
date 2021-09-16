package com.ask.springlockredisson.repository;

import com.ask.springlockredisson.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, String> {

  boolean existsByVoter(String Voter);
}
