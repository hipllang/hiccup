package com.mattmerr.hipllang.hiccup.parser;

import com.mattmerr.hipllang.hiccup.NameToken;
import com.mattmerr.hipllang.hiccup.ParseToken;
import com.mattmerr.hipllang.hiccup.SourceInfo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.mattmerr.hipllang.hiccup.parser.Grammar.isNameStarter;
import static com.mattmerr.hipllang.hiccup.parser.Grammar.isNameTail;
import static com.mattmerr.hipllang.hiccup.parser.Grammar.isPunctuation;
import static java.lang.Character.isWhitespace;

public class TokenStream {

  private final CharStream cs;
  private final String fileName;

  private ParseToken peek = null;

  public TokenStream(InputStream is, String fileName) {
    this.cs = new CharStream(is);
    this.fileName = fileName;
  }

  public TokenStream(String source, String fileName) {
    this(new ByteArrayInputStream(source.getBytes()), fileName);
  }

  SourceInfo sourceInfo() {
    return SourceInfo.newBuilder()
        .setFileName(fileName)
        .setLine(cs.line())
        .setColumn(cs.col())
        .build();
  }

  public ParseToken next() throws IOException {
    ParseToken ret = peek();
    peek = null;
    return ret;
  }

  public ParseToken peek() throws IOException {
    return peek == null ? (peek = readNext()) : peek;
  }

  private ParseToken readNext() throws IOException {
    eatAnyWhitespace();
    if (eof()) {
      return null;
    }

    ParseToken.Builder builder;
    SourceInfo sourceInfo = sourceInfo();

    char peek = cs.peek();
    if (isNameStarter(peek)) {
      builder = readName();
    } else if (isPunctuation(peek)) {
      builder = readPunctuation();
    } else {
      throw new RuntimeException("You're doing something wrong, idk: " + peek);
    }

    return builder
        .setSourceInfo(sourceInfo)
        .build();
  }

  private ParseToken.Builder readName() throws IOException {
    StringBuilder sb = new StringBuilder();

    while (!cs.eof()) {
      char ch = cs.peek();
      if (isNameTail(ch)) {
        sb.append(ch);
      } else {
        break;
      }
      cs.next();
    }

    return ParseToken.newBuilder()
        .setNameToken(NameToken.newBuilder()
            .setText(sb.toString()));
  }

  private ParseToken.Builder readPunctuation() throws IOException {
    char ch = cs.next();
    if (ch == '/') {
      if (cs.peek() == '/') {
        eatLineComment();
      }
    }
    return ParseToken.newBuilder().setPunctuation(Grammar.getPunctuationForChar(ch));
  }

  public boolean eof() {
    return cs.eof();
  }

  private void eatAnyWhitespace() throws IOException {
    while (!eof() && isWhitespace(cs.peek())) {
      cs.next();
    }
  }

  private void eatLineComment() throws IOException {
    while (!eof()) {
       if (cs.next() == '\n') {
         break;
       }
    }
  }

}
