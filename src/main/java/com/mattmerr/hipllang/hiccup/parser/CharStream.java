package com.mattmerr.hipllang.hiccup.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

class CharStream {

  private PushbackInputStream is;
  private int line, col;

  CharStream(InputStream is) {
    this.is = new PushbackInputStream(is);
    this.line = 0;
    this.col = 0;
  }

  Character next() throws IOException {
    if (is.available() == 0) {
      throw new RuntimeException("Unexpected EOF");
    }
    char read = (char) is.read();
    col += 1;
    if (read == '\n') {
      line += 1;
      col = 0;
    }
    return read;
  }

  Character peek() throws IOException {
    if (eof()) {
      return null;
    }
    char peeked = (char) is.read();
    is.unread(peeked);
    return peeked;
  }

  int line() {
    return line;
  }

  int col() {
    return col;
  }

  boolean eof() {
    try {
      return is.available() == 0;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
