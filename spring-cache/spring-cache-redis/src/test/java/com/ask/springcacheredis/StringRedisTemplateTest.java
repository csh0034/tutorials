package com.ask.springcacheredis;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class StringRedisTemplateTest {

  /**
   * @see org.springframework.data.redis.core.ValueOperationsEditor
   */
  @Resource(name="stringRedisTemplate")
  private ValueOperations<String, String> valueOperations;

//  @Autowired
//  private StringRedisTemplate stringRedisTemplate;

//  @BeforeEach
//  void setUp() {
//    valueOperations = stringRedisTemplate.opsForValue();
//  }

  @Test
  void setAndGet() {
    // given
    String key = "1.0.0:cache:type::redis";
    String value = "use";

    // when
    valueOperations.set(key, value, Duration.ofSeconds(30));

    // then
    String result = valueOperations.get(key);
    assertThat(result).isEqualTo(value);
  }

}
