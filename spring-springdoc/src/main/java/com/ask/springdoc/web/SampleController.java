package com.ask.springdoc.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "샘플 컨트롤러", description = "샘플 컨트롤러 설명")
@RestController
@Slf4j
public class SampleController {

  @Operation(description = "샘플 GET 메서드 설명", summary = "샘플 GET 메서드 서머리")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "샘플 GET 메서드 Response",
          content = @Content(examples = @ExampleObject(name = "ex name", description = "ex desc", summary = "ex summary", value = "ex value")))
  })
  @GetMapping("/sample")
  public String sample(@Parameter(description = "파라미터 설명", example = "샘플 id") String id) {
    log.info("id : {}", id);
    return "GET sample";
  }

  @Operation(description = "샘플 Post 메서드 설명", summary = "샘플 Post 메서드 서머리")
  @ApiResponse(responseCode = "200", description = "샘플 Post 메서드 Response")
  @PostMapping("/sample")
  public String sample(@Parameter(description = "파라미터 설명") @Valid @RequestBody SampleRequestVO requestVO) {
    log.info("requestVO : {}", requestVO);
    return "Post sample";
  }

  @Getter
  @Setter
  @ToString
  public static class SampleRequestVO {


    @Schema(description = "제목", example = "샘플 제목")
    @NotBlank
    @Size(min = 1, max = 20)
    String title;

    @Schema(description = "내용", example = "샘플 내용")
    @NotBlank
    @Size(min = 1, max = 1000)
    String content;
  }
}
