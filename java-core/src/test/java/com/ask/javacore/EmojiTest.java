package com.ask.javacore;

import static org.assertj.core.api.Assertions.assertThat;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class EmojiTest {

  @Test
  void parseToHtmlDecimal() {
    String input = "An 😀 🤔awesome 😃string with a few 😉emojis!";
    String resultDecimal = EmojiParser.parseToHtmlDecimal(input);

    assertThat(resultDecimal).isEqualTo("An &#128512; &#129300;awesome &#128515;string with a few &#128521;emojis!");
  }

  @Test
  void removeEmojis() {
    String input = "An 😀awe🤔some 😃string with a few 😉emojis!";
    List<Emoji> collection = new ArrayList<>();
    collection.add(EmojiManager.getForAlias("wink")); // This is 😉

    System.out.println(EmojiParser.removeAllEmojis(input));
    System.out.println(EmojiParser.removeAllEmojisExcept(input, collection));
    System.out.println(EmojiParser.removeEmojis(input, collection));
  }

  @Test
  void replaceAllEmojis() {
    String input = "An 😀awe🤔some 😃string with a few 😉emojis!";
    String result = EmojiParser.replaceAllEmojis(input, "#");
    assertThat(result).isEqualTo("An #awe#some #string with a few #emojis!");
  }

}
