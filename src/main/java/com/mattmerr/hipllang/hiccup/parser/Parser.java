package com.mattmerr.hipllang.hiccup.parser;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mattmerr.hipllang.hiccup.Block;
import com.mattmerr.hipllang.hiccup.BooleanLiteral;
import com.mattmerr.hipllang.hiccup.Expression;
import com.mattmerr.hipllang.hiccup.IfExpression;
import com.mattmerr.hipllang.hiccup.Name;
import com.mattmerr.hipllang.hiccup.ParseToken;
import com.mattmerr.hipllang.hiccup.Program;
import com.mattmerr.hipllang.hiccup.Punctuation;
import com.mattmerr.hipllang.hiccup.SourceInfo;
import java.io.IOException;
import java.util.Arrays;

public final class Parser {

  private Parser() {}

  public static Program parseProgram(TokenStream tokenStream) throws IOException {
    SourceInfo sourceInfo = tokenStream.sourceInfo();
    return Program.newBuilder()
        .setSourceInfo(sourceInfo)
        .setBlock(parseBlockBody(tokenStream))
        .build();
  }

  public static Expression parseExpression(TokenStream tokenStream) throws IOException {
    SourceInfo sourceInfo = tokenStream.sourceInfo();
    ParseToken first = tokenStream.peek();
    switch (first.getValueCase()) {
      case NAME_TOKEN:
        switch (first.getNameToken().getText()) {
          case "if":
            return Expression.newBuilder()
                .setSourceInfo(sourceInfo)
                .setIfExpression(parseIfExpression(tokenStream))
                .build();

          case "true":
            tokenStream.next();
            return Expression.newBuilder()
                .setSourceInfo(sourceInfo)
                .setBooleanLiteral(BooleanLiteral.newBuilder().setBooleanValue(true))
                .build();

          case "false":
            tokenStream.next();
            return Expression.newBuilder()
                .setSourceInfo(sourceInfo)
                .setBooleanLiteral(BooleanLiteral.newBuilder().setBooleanValue(false))
                .build();

          case "scope":
            tokenStream.next();
            eatPunctuation(tokenStream, Punctuation.OPEN_CURLY);
            Block block = parseBlockBody(tokenStream);
            eatPunctuation(tokenStream, Punctuation.CLOSE_CURLY);
            return Expression.newBuilder()
                .setSourceInfo(sourceInfo)
                .setBlock(block)
                .build();

          default:
            tokenStream.next();
            return Expression.newBuilder()
                .setSourceInfo(sourceInfo)
                .setName(Name.newBuilder().setValue(first.getNameToken().getText()))
                .build();
        }

      default:
        break;
    }
    throw new IllegalArgumentException("IDK this yet");
  }

  public static Block parseBlockBody(TokenStream tokenStream) throws IOException {
    Block.Builder builder = Block.newBuilder();
    ParseToken peek = tokenStream.peek();
    while (!tokenStream.eof() &&
        (peek.getValueCase() != ParseToken.ValueCase.PUNCTUATION
            || peek.getPunctuation() != Punctuation.CLOSE_CURLY)) {
      builder.addExpression(parseExpression(tokenStream));
      eatPunctuation(tokenStream, Punctuation.SEMICOLON);
      peek = tokenStream.peek();
    }
    return builder.build();
  }

  public static IfExpression parseIfExpression(TokenStream tokenStream) throws IOException {
    IfExpression.Builder builder = IfExpression.newBuilder();
    eatName(tokenStream, "if");
    eatPunctuation(tokenStream, Punctuation.OPEN_PAREN);
    builder.setCondition(parseExpression(tokenStream));
    eatPunctuation(tokenStream, Punctuation.CLOSE_PAREN);
    eatPunctuation(tokenStream, Punctuation.OPEN_CURLY);
    builder.setIfTrue(
        Expression.newBuilder()
            .setSourceInfo(tokenStream.sourceInfo())
            .setBlock(parseBlockBody(tokenStream)));
    eatPunctuation(tokenStream, Punctuation.CLOSE_CURLY);
    ParseToken maybeElse = tokenStream.peek();
    if (maybeElse != null
        && maybeElse.hasNameToken()
        && "else".equals(maybeElse.getNameToken().getText())) {
      tokenStream.next();
      eatPunctuation(tokenStream, Punctuation.OPEN_CURLY);
      builder.setIfFalse(
          Expression.newBuilder()
              .setSourceInfo(tokenStream.sourceInfo())
              .setBlock(parseBlockBody(tokenStream)));
      eatPunctuation(tokenStream, Punctuation.CLOSE_CURLY);
    }
    return builder.build();
  }

  private static void eatName(TokenStream tokenStream, String name) throws IOException {
    checkNotNull(tokenStream);
    checkNotNull(name);
    ParseToken tok = tokenStream.next();
    if (!tok.hasNameToken() || !name.equals(tok.getNameToken().getText())) {
      throw new AssertionError("TODO"); // TODO: something descriptive
    }
  }

  private static void eatPunctuation(TokenStream tokenStream, Punctuation... p) throws IOException {
    checkNotNull(tokenStream);
    checkNotNull(p);
    ParseToken tok = tokenStream.next();
    if (tok == null) {
      throw new AssertionError("Expected one of " + Arrays.toString(p) + " found EOF");
    }
    if (tok.getValueCase() != ParseToken.ValueCase.PUNCTUATION
        || !Arrays.asList(p).contains(tok.getPunctuation())) {
      throw new AssertionError("Expected one of " + Arrays.toString(p) + " found " + tok);
    }
  }

}
