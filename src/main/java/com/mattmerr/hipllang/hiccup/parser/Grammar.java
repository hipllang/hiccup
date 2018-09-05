package com.mattmerr.hipllang.hiccup.parser;

import com.mattmerr.hipllang.hiccup.Punctuation;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public final class Grammar {

  public static boolean isNameStarter(char ch) {
    return ('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z');
  }

  public static boolean isNameTail(char ch) {
    return ('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z') || ('0' <= ch && ch <= '9');
  }

  private static EnumMap<Punctuation, Character> punctuationChars = new EnumMap<>(Punctuation.class);
  private static Map<Character, Punctuation> punctuationByChar = new HashMap<>();

  private static void registerPunctuationMapping(Punctuation p, char c) {
    punctuationChars.put(p, c);
    punctuationByChar.put(c, p);
  }

  static {
    registerPunctuationMapping(Punctuation.AMPERSAND, '&');
    registerPunctuationMapping(Punctuation.BANG, '!');
    registerPunctuationMapping(Punctuation.CARAT, '^');
    registerPunctuationMapping(Punctuation.COMMA, ',');
    registerPunctuationMapping(Punctuation.EQUALS, '=');
    registerPunctuationMapping(Punctuation.MINUS, '-');
    registerPunctuationMapping(Punctuation.PERCENT, '%');
    registerPunctuationMapping(Punctuation.PERIOD, '.');
    registerPunctuationMapping(Punctuation.PIPE, '|');
    registerPunctuationMapping(Punctuation.PLUS, '+');
    registerPunctuationMapping(Punctuation.SEMICOLON, ';');
    registerPunctuationMapping(Punctuation.STAR, '*');
    registerPunctuationMapping(Punctuation.TILDE, '~');
    registerPunctuationMapping(Punctuation.OPEN_ANGLE, '<');
    registerPunctuationMapping(Punctuation.CLOSE_ANGLE, '>');
    registerPunctuationMapping(Punctuation.OPEN_CURLY, '{');
    registerPunctuationMapping(Punctuation.CLOSE_CURLY, '}');
    registerPunctuationMapping(Punctuation.OPEN_PAREN, '(');
    registerPunctuationMapping(Punctuation.CLOSE_PAREN, ')');
    registerPunctuationMapping(Punctuation.OPEN_SQUARE, '[');
    registerPunctuationMapping(Punctuation.CLOSE_SQUARE, ']');
    registerPunctuationMapping(Punctuation.FORWARD_SLASH, '/');
    registerPunctuationMapping(Punctuation.BACKWARD_SLASH, '\\');
  }

  public static Character getCharForPunctuation(Punctuation p) {
    return punctuationChars.get(p);
  }

  public static Punctuation getPunctuationForChar(char ch) {
    return punctuationByChar.get(ch);
  }

  public static boolean isPunctuation(char ch) {
    return punctuationByChar.containsKey(ch);
  }
}
