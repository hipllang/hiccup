package com.mattmerr.hipllang.hiccup.parser;

import com.mattmerr.hipllang.hiccup.Punctuation;

import org.junit.Test;

import static com.google.common.truth.Truth.assertWithMessage;

public class GrammarTest {

  @Test
  public void testGrammarPunctuationCoverage() {
    for (Punctuation p : Punctuation.values()) {
      if (p != Punctuation.UNRECOGNIZED) {
        assertWithMessage("Grammar doesn't recognize " + p.toString())
            .that(Grammar.getCharForPunctuation(p))
            .isNotNull();
      }
    }
  }
}
