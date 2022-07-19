package com.ask.googleguava;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import com.google.common.base.Preconditions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class PreconditionsTests {

  private static final String NON_NULL_STRING = "foo";

  @Nested
  class CheckArgument {

    @Test
    public void testCheckArgument_simple_success() {
      Preconditions.checkArgument(true);
    }

    @Test
    public void testCheckArgument_simple_failure() {
      try {
        Preconditions.checkArgument(false);
        fail("no exception thrown");
      } catch (IllegalArgumentException expected) {
      }
    }

    @Test
    public void testCheckArgument_nullMessage_failure() {
      try {
        Preconditions.checkArgument(false, null);
        fail("no exception thrown");
      } catch (IllegalArgumentException expected) {
        assertThat(expected).message().isEqualTo("null");
      }
    }

    @Test
    public void testCheckArgument_tooManyArgs_failure() {
      try {
        Preconditions.checkArgument(false, "A %s C %s E", "b", "d", "f");
        fail("no exception thrown");
      } catch (IllegalArgumentException e) {
        assertThat(e).message().isEqualTo("A b C d E [f]");
      }
    }

  }

  @Nested
  class CheckNotNull {

    @Test
    public void testCheckNotNull_simple_success() {
      String result = Preconditions.checkNotNull(NON_NULL_STRING);
      assertThat(result).isEqualTo(NON_NULL_STRING);
    }

    @Test
    public void testCheckNotNull_simple_failure() {
      try {
        Preconditions.checkNotNull(null);
        fail("no exception thrown");
      } catch (NullPointerException expected) {
      }
    }

    @Test
    public void testCheckNotNull_simpleMessage_success() {
      String result = Preconditions.checkNotNull(NON_NULL_STRING, "IGNORE_ME");
      assertThat(result).isEqualTo(NON_NULL_STRING);
    }

  }

}
