package com.ask.awss3;

import io.awspring.cloud.core.io.s3.PathMatchingSimpleStorageResourcePatternResolver;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

@SpringBootTest
@Slf4j
public class ResourcePatternResolverTest {

  @Autowired
  private PathMatchingSimpleStorageResourcePatternResolver resourcePatternResolver;

  @DisplayName("s3 bucket 파일 조회")
  @Test
  void searchResources() throws IOException {
    List<Resource> allFilesInFolder = toList(resourcePatternResolver.getResources("s3://my-bucket/*"));
    List<Resource> allFilesInBucket = toList(resourcePatternResolver.getResources("s3://my-bucket/**/*"));
    List<Resource> allFilesGlobally = toList(resourcePatternResolver.getResources("s3://**/*"));

    log.info("allFilesInFolder, s3://my-bucket/* {}", allFilesInFolder);
    log.info("allFilesInBucket, s3://my-bucket/**/* {}", allFilesInBucket);
    log.info("allFilesGlobally, s3://**/* {}", allFilesGlobally);
  }

  private static <T> List<T> toList(T[] array) {
    return Arrays.stream(array).collect(Collectors.toList());
  }

}
