package com.ask.springjpaquerydsl;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ClassifyTest {

  private static final Random RANDOM = new Random();

  @Test
  void classify() {
    // given
    List<FlatUserVO> flatUsers = IntStream.rangeClosed(1, 1000)
        .mapToObj(i -> new FlatUserVO("company" + randomOneThree(), "user" + i, "userName" + i))
        .collect(toList());

    // when
    List<UserVO> userVOList = classify(flatUsers);

    // then
    userVOList.forEach(userVO -> log.info("userVO : {}", userVO));
  }

  private int randomOneThree() {
    return RANDOM.nextInt(3) + 1;
  }

  private List<UserVO> classify(List<FlatUserVO> flatUsers) {
    Map<UserVO, List<UserDataVO>> classified = new LinkedHashMap<>();

    for (FlatUserVO flatUser : flatUsers) {
      UserVO userVO = new UserVO(flatUser);
      UserDataVO userDataVO = new UserDataVO(flatUser);

      List<UserDataVO> users = classified.get(userVO);

      if (users == null) {
        classified.put(userVO, new ArrayList<>(Collections.singletonList(userDataVO)));
      } else {
        users.add(userDataVO);
      }
    }

    return classified.entrySet().stream()
        .map(entry -> {
          UserVO userVO = entry.getKey();
          userVO.users = entry.getValue();
          return userVO;
        })
        .collect(toList());
  }

  @ToString
  @AllArgsConstructor
  public static class FlatUserVO {

    private String companyId;
    private String userId;
    private String userName;
  }

  @ToString
  @AllArgsConstructor
  @EqualsAndHashCode(of = "companyId")
  public static class UserVO {

    private String companyId;
    private List<UserDataVO> users;

    public UserVO(FlatUserVO flatUserVO) {
      this.companyId = flatUserVO.companyId;
    }
  }

  @ToString
  @AllArgsConstructor
  @EqualsAndHashCode(of = "userId")
  public static class UserDataVO {

    private String userId;
    private String userName;

    public UserDataVO(FlatUserVO flatUserVO) {
      this.userId = flatUserVO.userId;
      this.userName = flatUserVO.userName;
    }
  }
}
