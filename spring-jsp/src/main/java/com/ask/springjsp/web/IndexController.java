package com.ask.springjsp.web;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

  @GetMapping("/")
  public String index() {
    return "index";
  }

  @GetMapping("/xml")
  @ResponseBody
  public XmlObj xml() {
    return new XmlObj("ask", 20);
  }


  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
  @XmlRootElement(name = "RESULT")
  @XmlAccessorType(XmlAccessType.FIELD)
  @NoArgsConstructor
  @AllArgsConstructor
  public static class XmlObj{
    private String name;
    private Integer age;
  }
}