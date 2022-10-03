package com.ask.springcore.config.converter;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;

@SuppressWarnings("rawtypes")
public class LenientStringToEnumConverterFactory implements ConverterFactory<String, Enum<?>> {

  private final Map<Class<?>, Converter> converterCache = new ConcurrentReferenceHashMap<>();

  @SuppressWarnings("unchecked")
  @Override
  public <E extends Enum<?>> Converter<String, E> getConverter(Class<E> targetType) {
    Class<?> enumType = targetType;
    while (enumType != null && !enumType.isEnum()) {
      enumType = enumType.getSuperclass();
    }
    Assert.notNull(enumType, () -> "The target type " + targetType.getName() + " does not refer to an enum");

    Converter converter = converterCache.get(enumType);
    if (converter == null) {
      converter = new LenientToEnumConverter<>((Class<E>) enumType);
      converterCache.put(enumType, converter);
    }
    return converter;
  }

  @SuppressWarnings("unchecked")
  private static class LenientToEnumConverter<E extends Enum> implements Converter<String, E> {

    private final Class<E> enumType;
    private Map<String, Enum<? extends Code>> codeMap = Collections.emptyMap();

    private LenientToEnumConverter(Class<E> enumType) {
      this.enumType = enumType;

      if (Code.class.isAssignableFrom(enumType)) {
        setCodeMap(enumType);
      }
    }

    private void setCodeMap(Class<E> enumType) {
      this.codeMap = Arrays.stream(enumType.getEnumConstants())
          .collect(toMap(o -> ((Code) o).getCode(), o -> o));
    }

    @Override
    public E convert(String source) {
      String value = source.trim();
      if (value.isEmpty()) {
        return null;
      }
      try {
        return (E) Enum.valueOf(this.enumType, value);
      } catch (Exception ex) {
        if (!codeMap.isEmpty()) {
          Enum<? extends Code> result = codeMap.get(value);
          if (result != null) {
            return (E) result;
          }
        }
        return findEnum(value);
      }
    }

    private E findEnum(String value) {
      String name = convertToLenientName(value);
      for (E candidate : (Set<E>) EnumSet.allOf(this.enumType)) {
        String candidateName = convertToLenientName(candidate.name());
        if (name.equals(candidateName)) {
          return candidate;
        }
      }
      throw new IllegalArgumentException("No enum constant " + this.enumType.getCanonicalName() + "." + value);
    }

    private String convertToLenientName(String name) {
      StringBuilder canonicalName = new StringBuilder(name.length());
      name.chars()
          .filter(Character::isLetterOrDigit)
          .map(Character::toLowerCase)
          .forEach((c) -> canonicalName.append((char) c));
      return canonicalName.toString();
    }

  }

}
