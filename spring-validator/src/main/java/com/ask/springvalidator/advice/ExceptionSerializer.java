package com.ask.springvalidator.advice;

import com.ask.springvalidator.vo.FieldError;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;

@JsonComponent
@RequiredArgsConstructor
public class ExceptionSerializer extends JsonObjectSerializer<BindingResult> {

  private final MessageSource messageSource;

  @Override
  protected void serializeObject(BindingResult bindingResult, JsonGenerator jsonGenerator, SerializerProvider provider)
      throws IOException {
    List<FieldError> fieldErrors = bindingResult.getFieldErrors()
        .stream()
        .map(error -> FieldError.create(
            error.getField(),
            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
            messageSource.getMessage(error, Locale.getDefault())))
        .collect(Collectors.toList());

//    jsonGenerator.useDefaultPrettyPrinter();
    jsonGenerator.writeNumberField("timestamp", System.currentTimeMillis());
    jsonGenerator.writeStringField("code", "40999");
    jsonGenerator.writeStringField("message", "Invalid Input Value");
    jsonGenerator.writeObjectField("fieldErrors", fieldErrors);
  }

}
