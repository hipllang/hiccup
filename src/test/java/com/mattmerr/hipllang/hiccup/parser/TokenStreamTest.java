package com.mattmerr.hipllang.hiccup.parser;

import com.mattmerr.hipllang.hiccup.ParseToken;
import com.mattmerr.hipllang.hiccup.Punctuation;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class TokenStreamTest {

  @Test
  public void testBasic() throws IOException {
    TokenStream ts = new TokenStream("print", "test");

    ParseToken peek = ts.peek();
    assertThat(peek).isNotNull();
    assertThat(peek.getValueCase()).isEqualTo(ParseToken.ValueCase.NAME_TOKEN);
    assertThat(peek.getNameToken().getText()).isEqualTo("print");

    ParseToken next = ts.next();
    assertThat(next).isEqualTo(peek);
  }

  @Test
  public void testPunctuation() throws IOException {
    TokenStream ts = new TokenStream("~!%^&*()=+{}[]|\\;<>,./", "test");
    List<Punctuation> parsedList = new ArrayList<>();
    int col = 0;
    while (!ts.eof()) {
      ParseToken tok = ts.next();
      assertThat(tok.getSourceInfo().getFileName()).isEqualTo("test");
      assertThat(tok.getSourceInfo().getLine()).isEqualTo(0);
      assertThat(tok.getSourceInfo().getColumn()).isEqualTo(col);
      assertThat(tok.getValueCase()).isEqualTo(ParseToken.ValueCase.PUNCTUATION);
      parsedList.add(tok.getPunctuation());
      col += 1;
    }
    assertThat(parsedList)
        .containsExactly(
            Punctuation.TILDE,
            Punctuation.BANG,
            Punctuation.PERCENT,
            Punctuation.PERCENT,
            Punctuation.CARAT,
            Punctuation.AMPERSAND,
            Punctuation.STAR,
            Punctuation.OPEN_PAREN,
            Punctuation.CLOSE_PAREN,
            Punctuation.EQUALS,
            Punctuation.PLUS,
            Punctuation.OPEN_CURLY,
            Punctuation.CLOSE_CURLY,
            Punctuation.OPEN_SQUARE,
            Punctuation.CLOSE_SQUARE,
            Punctuation.PIPE,
            Punctuation.BACKWARD_SLASH,
            Punctuation.SEMICOLON,
            Punctuation.OPEN_ANGLE,
            Punctuation.CLOSE_ANGLE,
            Punctuation.COMMA,
            Punctuation.PERCENT,
            Punctuation.FORWARD_SLASH);
  }

}
