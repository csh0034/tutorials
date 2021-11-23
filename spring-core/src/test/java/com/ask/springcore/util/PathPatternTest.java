package com.ask.springcore.util;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.PathContainer.Element;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class PathPatternTest {

  @Test
  public void pathContainer() {
    assertThat(elementsToString(toPathContainer("/abc/def").elements())).isEqualTo("[/][abc][/][def]");
    assertThat(elementsToString(toPathContainer("abc/def").elements())).isEqualTo("[abc][/][def]");
    assertThat(elementsToString(toPathContainer("abc/def/").elements())).isEqualTo("[abc][/][def][/]");
    assertThat(elementsToString(toPathContainer("abc//def//").elements())).isEqualTo("[abc][/][/][def][/][/]");
    assertThat(elementsToString(toPathContainer("/").elements())).isEqualTo("[/]");
    assertThat(elementsToString(toPathContainer("///").elements())).isEqualTo("[/][/][/]");
  }

  @Test
  void matchingLiteralPathElement() {
    checkMatches("foo", "foo");
    checkNoMatch("foo", "bar");
    checkNoMatch("foo", "/foo");
    checkNoMatch("/foo", "foo");
    checkMatches("/f", "/f");
    checkMatches("/foo", "/foo");
    checkNoMatch("/foo", "/food");
    checkNoMatch("/food", "/foo");
    checkMatches("/foo/", "/foo/");
    checkMatches("/foo/bar/woo", "/foo/bar/woo");
    checkMatches("foo/bar/woo", "foo/bar/woo");
  }

  @Test
  void basicMatching() {
    checkMatches("", "");
    checkMatches("", "/");
    checkMatches("", null);
    checkNoMatch("/abc", "/");
    checkMatches("/", "/");
    checkNoMatch("/", "/a");
    checkMatches("foo/bar/", "foo/bar/");
    checkNoMatch("foo", "foobar");
    checkMatches("/foo/bar", "/foo/bar");
    checkNoMatch("/foo/bar", "/foo/baz");
  }

  private PathContainer toPathContainer(String path) {
    if (path == null) {
      return null;
    }
    return PathContainer.parsePath(path);
  }

  private String elementsToString(List<Element> elements) {
    return elements.stream()
        .map(element -> String.format("[%s]", element.value()))
        .collect(joining());
  }

  private void checkMatches(String uriTemplate, String path) {
    PathPatternParser parser = new PathPatternParser();
    parser.setMatchOptionalTrailingSeparator(true); // 마지막에 슬래쉬 있어도 매칭 처리하는 옵션
    PathPattern pattern = parser.parse(uriTemplate);
    PathContainer pathContainer = toPathContainer(path);
    assertThat(pattern.matches(pathContainer)).isTrue();
  }

  private void checkNoMatch(String uriTemplate, String path) {
    PathPatternParser parser = new PathPatternParser();
    PathPattern pattern = parser.parse(uriTemplate);
    PathContainer pathContainer = toPathContainer(path);
    assertThat(pattern.matches(pathContainer)).isFalse();
  }
}
