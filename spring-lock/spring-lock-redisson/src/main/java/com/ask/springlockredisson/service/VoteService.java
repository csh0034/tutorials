package com.ask.springlockredisson.service;

import com.ask.springlockredisson.entity.Vote;
import com.ask.springlockredisson.repository.VoteRepository;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VoteService {

  private final VoteRepository voteRepository;
  private final RedissonClient redissonClient;

  public void voteWithoutLock(String voter, String candidate) {
    if (voteRepository.existsByVoter(voter)) {
      log.error("{} already voted", voter);
    } else {
      voteRepository.saveAndFlush(Vote.create(voter, candidate));
      log.info("{} vote completed", voter);
    }
  }

  public void voteWithLock(String voter, String candidate) {
    RLock lock = redissonClient.getLock("voter-" + voter);
    try {
      if (lock.tryLock(15, 10, TimeUnit.SECONDS)) {
        try {
          if (voteRepository.existsByVoter(voter)) {
            log.error("{} already voted", voter);
          } else {
            voteRepository.saveAndFlush(Vote.create(voter, candidate)); // 반드시 saveAndFlush 해야함
            log.info("{} vote completed", voter);
          }
        } finally {
          if (lock.isLocked())
            lock.unlock();
        }
      }
    } catch(InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public List<Vote> findAll() {
    return voteRepository.findAll();
  }

  public void deleteAll() {
    voteRepository.deleteAll();
  }
}
