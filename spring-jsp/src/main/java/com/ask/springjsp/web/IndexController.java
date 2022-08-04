package com.ask.springjsp.web;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.time.LocalDateTime;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class IndexController {

  private final ServletContext servletContext;
  private final HttpServletRequest request;
  private final HttpSession session;

  @GetMapping("/")
  public String index(Model model) {
    LocalDateTime now = LocalDateTime.now();

    servletContext.setAttribute("servletContextNow", now);
    request.setAttribute("requestNow", now);
    session.setAttribute("sessionNow", now);
    model.addAttribute("modelNow", now);

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
