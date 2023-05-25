package com.ask.springjpacore;

import io.hypersistence.tsid.TSID;
import io.hypersistence.tsid.TSID.Factory;
import java.time.Instant;
import java.util.function.IntFunction;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.RepeatedTest;

@Slf4j
class TsidTest {

  @RepeatedTest(10)
  void tsid() {
    TSID tsid = Factory.getTsid();
    log.info("tsid long: {}, tsid string: {}", tsid.toLong(), tsid);

  }

  @RepeatedTest(10)
  void snowflake() {
    // Twitter Snowflakes have 5 bits for datacenter ID and 5 bits for worker ID
    int datacenter = 21; // max: 2^5-1 = 31
    int worker = 11;     // max: 2^5-1 = 31
    int node = (datacenter << 5 | worker); // max: 2^10-1 = 1023

    // Twitter Epoch is fixed in 1288834974657 (2010-11-04T01:42:54.657Z)
    Instant customEpoch = Instant.ofEpochMilli(1288834974657L);

    // a function that returns an array with ZEROS, making the factory
    // to RESET the counter to ZERO when the millisecond changes
    IntFunction<byte[]> randomFunction = byte[]::new;

    // a factory that returns TSIDs similar to Twitter Snowflakes
    TSID.Factory factory = TSID.Factory.builder()
        .withRandomFunction(randomFunction)
        .withCustomEpoch(customEpoch)
        .withNode(node)
        .build();

    // use the factory
    long snowflakeId = factory.generate().toLong();
    log.info("tsid: {}, timestamp: {}", snowflakeId, extractTimestampFromSnowflakeId(snowflakeId));
  }

  private long extractTimestampFromSnowflakeId(long snowFlakeId) {
    return (snowFlakeId >> 22) + 1288834974657L;
  }

}
