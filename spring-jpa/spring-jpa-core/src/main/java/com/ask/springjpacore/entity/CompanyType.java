package com.ask.springjpacore.entity;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CompanyType {

  AAA("A"),
  BBB("B"),
  CCC("C");

  private static final Map<String, CompanyType> CACHE = Arrays.stream(values())
      .collect(toMap(CompanyType::getCode, Function.identity()));

  private final String code;

  @Converter(autoApply = true)
  public static class CompanyTypeConverter implements AttributeConverter<CompanyType, String> {

    @Override
    public String convertToDatabaseColumn(CompanyType companyType) {
      return companyType.code;
    }

    @Override
    public CompanyType convertToEntityAttribute(String code) {
      if (!CACHE.containsKey(code)) {
        throw new IllegalArgumentException(String.format("'%s' is not exists", code));
      }
      return CACHE.get(code);
    }

  }

}
