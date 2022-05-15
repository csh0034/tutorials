package com.ask.springjasypt.config;

import static com.ulisesbocchio.jasyptspringboot.properties.JasyptEncryptorConfigurationProperties.bindConfigProps;

import com.ulisesbocchio.jasyptspringboot.configuration.EnvCopy;
import com.ulisesbocchio.jasyptspringboot.configuration.StringEncryptorBuilder;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * StringEncryptor Bean 을 설정하지 말고 property 설정을 기반으로 DefaultLazyEncryptor 를 사용하는것이 좋다.
 */
@Configuration
public class JasyptConfig {

  @Bean
  public StringEncryptor jasyptStringEncryptor(EnvCopy envCopy) {
    return new StringEncryptorBuilder(bindConfigProps(envCopy.get()), "jasypt.encryptor")
        .build();
  }

//  @Bean
//  public StringEncryptor jasyptStringEncryptor() {
//    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
//    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
//    config.setPassword("password");
//    config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
//    config.setKeyObtentionIterations("1000");
//    config.setPoolSize("1");
//    config.setProviderName("SunJCE");
//    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
//    config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
//    config.setStringOutputType("base64");
//    encryptor.setConfig(config);
//    return encryptor;
//  }

}
