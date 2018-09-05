package com.mattmerr.hipllang.hiccup.parser;

import com.mattmerr.hipllang.hiccup.Expression;
import com.mattmerr.hipllang.hiccup.IfExpression;

import com.google.protobuf.TextFormat;
import org.junit.Test;

import java.io.IOException;

import static com.google.common.truth.Truth.assertThat;

public class ParserTest {

  @Test
  public void testIfStatement() throws IOException {
    TokenStream tokenStream = new TokenStream("if (something) { true; }", "test");
    Expression expression = Parser.parseExpression(tokenStream);
    assertThat(expression.getValueCase()).isEqualTo(Expression.ValueCase.IF_EXPRESSION);
    IfExpression ifExp = expression.getIfExpression();
    assertThat(ifExp.getCondition().getName().getValue()).isEqualTo("something");
    Expression ifTrueExp = ifExp.getIfTrue();
    assertThat(ifTrueExp.getBlock().getExpression(0).getBooleanLiteral().getBooleanValue())
        .isTrue();
  }

  @Test
  public void testIfElseStatement() throws IOException {
    TokenStream tokenStream = new TokenStream("if (something) { true; } else { false; }", "test");
    Expression expression = Parser.parseExpression(tokenStream);
    Expression expected =
        TextFormat.parse(
            "source_info {\n"
                + "  file_name: \"test\"\n"
                + "  line: 0\n"
                + "  column: 0\n"
                + "}\n"
                + "if_expression {\n"
                + "  condition {\n"
                + "    source_info {\n"
                + "      file_name: \"test\"\n"
                + "      line: 0\n"
                + "      column: 4\n"
                + "    }\n"
                + "    name {\n"
                + "      value: \"something\"\n"
                + "    }\n"
                + "  }\n"
                + "  if_true {\n"
                + "    source_info {\n"
                + "      file_name: \"test\"\n"
                + "      line: 0\n"
                + "      column: 16\n"
                + "    }\n"
                + "    block {\n"
                + "      expression {\n"
                + "        source_info {\n"
                + "          file_name: \"test\"\n"
                + "          line: 0\n"
                + "          column: 21\n"
                + "        }\n"
                + "        boolean_literal {\n"
                + "          boolean_value: true\n"
                + "        }\n"
                + "      }\n"
                + "    }\n"
                + "  }\n"
                + "  if_false {\n"
                + "    source_info {\n"
                + "      file_name: \"test\"\n"
                + "      line: 0\n"
                + "      column: 31\n"
                + "    }\n"
                + "    block {\n"
                + "      expression {\n"
                + "        source_info {\n"
                + "          file_name: \"test\"\n"
                + "          line: 0\n"
                + "          column: 37\n"
                + "        }\n"
                + "        boolean_literal {\n"
                + "        }\n"
                + "      }\n"
                + "    }\n"
                + "  }\n"
                + "}",
            Expression.class);
    assertThat(expression).isEqualTo(expected);
  }

}