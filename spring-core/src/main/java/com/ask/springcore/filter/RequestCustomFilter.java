package com.ask.springcore.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.GenericFilterBean;

@Component
@Slf4j
public class RequestCustomFilter extends GenericFilterBean {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    ReusableRequestWrapper wrapper = new ReusableRequestWrapper((HttpServletRequest) request);

    log.info("body: {}", wrapper.getBody());

    chain.doFilter(wrapper, response);
  }

  public static class ReusableRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] contents;

    public ReusableRequestWrapper(HttpServletRequest request) {
      super(request);

      try (InputStream inputStream = request.getInputStream()) {
        this.contents = StreamUtils.copyToByteArray(inputStream);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public ServletInputStream getInputStream() {
      return new CachedServletInputStream(this.contents);
    }

    @Override
    public BufferedReader getReader() {
      return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }

    public String getBody() {
      return new String(contents, StandardCharsets.UTF_8);
    }

  }

  private static class CachedServletInputStream extends ServletInputStream {

    private final ByteArrayInputStream buffer;

    public CachedServletInputStream(byte[] contents) {
      this.buffer = new ByteArrayInputStream(contents);
    }

    @Override
    public int read() {
      return buffer.read();
    }

    @Override
    public boolean isFinished() {
      return buffer.available() == 0;
    }

    @Override
    public boolean isReady() {
      return true;
    }

    @Override
    public void setReadListener(ReadListener listener) {
      throw new UnsupportedOperationException();
    }

  }

}
