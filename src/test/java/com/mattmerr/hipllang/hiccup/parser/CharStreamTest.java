package com.mattmerr.hipllang.hiccup.parser;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.google.common.truth.Truth.assertThat;

public class CharStreamTest {

  @Test
  public void testBasic() throws IOException {
    CharStream cs = new CharStream(new ByteArrayInputStream("testing".getBytes()));
    assertThat(cs.eof()).isFalse();
    assertThat(cs.peek()).isEqualTo('t');
    assertThat(cs.next());
  }
}
