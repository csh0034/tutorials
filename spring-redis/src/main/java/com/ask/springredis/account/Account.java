package com.ask.springredis.account;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash
@Getter
@ToString
public class Account {

  @Id
  private String id;

  private String name;

  @TimeToLive
  private long expireSecond;

  public static Account create(String name, long expireSecond) {
    Account account = new Account();
    account.name = name;
    account.expireSecond = expireSecond;
    return account;
  }

}
