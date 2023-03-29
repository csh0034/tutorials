package com.ask.springdoc.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SpringDocConfig {

  public static final String SECURITY_SCHEME_KEY = "Authorization-Jwt";

  @Value("${springdoc.version}")
  private String version;

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(info())
        .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_KEY))
        .components(components());
  }

  private Info info() {
    return new Info()
        .title("Foo API")
        .description("설명...")
        .version(version)
        .contact(new Contact().name("ASk").url("https://naver.com/").email("test@gmail.com"))
        .license(new License().name("Apache License Version 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"));
  }

  private Components components() {
    return new Components()
        .addSecuritySchemes(SECURITY_SCHEME_KEY, authorization());
  }

  private SecurityScheme authorization() {
    return new SecurityScheme()
        .type(Type.APIKEY)
        .name(HttpHeaders.AUTHORIZATION)
        .in(SecurityScheme.In.HEADER);
  }

}
