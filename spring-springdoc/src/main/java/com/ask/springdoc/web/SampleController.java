package com.ask.springdoc.web;

import com.ask.springdoc.config.SpringDocConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "샘플 컨트롤러", description = "샘플 컨트롤러 설명")
@SecurityRequirement(name = SpringDocConfig.SECURITY_SCHEME_KEY)
@RestController
@Slf4j
public class SampleController {

  @Operation(description = "샘플 GET 메서드 설명", summary = "Get, Url Query String")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "샘플 GET 메서드 Response",
          content = @Content(examples = @ExampleObject(name = "ex name", description = "ex desc", summary = "ex summary", value = "ex value")))
  })
  @Parameter(name = "X-Sample", in = ParameterIn.HEADER, schema = @Schema(type = "string"))
  @GetMapping("/get")
  public String sample(@Parameter(description = "파라미터 설명", example = "샘플 id") @RequestParam(required = false) String id) {
    log.info("id : {}", id);
    return "GET sample";
  }

  @Operation(description = "샘플 Post 메서드 설명, application/json", summary = "Post, Body Json")
  @ApiResponse(responseCode = "200", description = "샘플 Post 메서드 Response")
  @PostMapping("/json")
  public String json(@Parameter(description = "파라미터 설명") @Valid @RequestBody SampleRequestVO requestVO) {
    log.info("requestVO : {}", requestVO);
    return "Post sample";
  }

  @Operation(description = "샘플 Post, application/x-www-form-urlencoded", summary = "Post, Url Query String")
  @ApiResponse(responseCode = "200", description = "샘플 Post 메서드 Response2")
  @PostMapping(value = "/urlQueryString", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public String urlQueryString(@Valid UrlQueryStringVO requestVO) {
    log.info("requestVO : {}", requestVO);
    return "Post sample";
  }

  @Operation(description = "샘플 Post, application/x-www-form-urlencoded", summary = "Post, Body Query String")
  @ApiResponse(responseCode = "200", description = "샘플 Post 메서드 Response2")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE))
  @PostMapping(value = "/bodyQueryString")
  public String bodyQueryString(@Valid BodyQueryStringVO requestVO) {
    log.info("requestVO : {}", requestVO);
    return "Post sample";
  }

  @Operation(description = "샘플 Post, multipart/form-data", summary = "Post, Multipart")
  @ApiResponse(responseCode = "200", description = "샘플 Post 메서드 Response3")
//  @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = MultipartVO.class), mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
  @PostMapping(value = "/multipart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public String multipart(@Valid MultipartVO requestVO) {
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
    private String title;

    @Schema(description = "내용", example = "샘플 내용")
    @NotBlank
    @Size(min = 1, max = 1000)
    private String content;

    @Schema(description = "데이터")
    private Data data;

    @Getter
    @Setter
    @ToString
    @Schema(name = "SampleRequestVO.Data")
    public static class Data {

      @Schema(description = "데이터", example = "샘플 데이터")
      @NotBlank
      @Size(min = 1, max = 1000)
      String data;

    }
  }

  /**
   *  request post json body 가 아닌 파라미터 인경우 ParameterObject 추가해야한다.
   */
  @Getter
  @Setter
  @ToString
  @ParameterObject
  public static class UrlQueryStringVO {

    @Parameter(description = "제목", example = "샘플 제목")
    @Size(min = 1, max = 20)
    private String title;

    @Parameter(description = "내용", example = "샘플 내용")
    @NotBlank
    @Size(min = 1, max = 1000)
    private String content;

  }

  @Getter
  @Setter
  @ToString
  public static class BodyQueryStringVO {

    @Schema(description = "제목", example = "샘플 제목")
    @Size(min = 1, max = 20)
    private String title;

    @Schema(description = "내용", example = "샘플 내용")
    @NotBlank
    @Size(min = 1, max = 1000)
    private String content;

  }

  @Getter
  @Setter
  @ToString
  public static class MultipartVO {

    @Schema(description = "제목", example = "샘플 제목")
    @Size(min = 1, max = 20)
    private String title;

    @Schema(description = "내용", example = "샘플 내용")
    @NotBlank
    @Size(min = 1, max = 1000)
    private String content;

    private MultipartFile multipartFile;

  }

}
