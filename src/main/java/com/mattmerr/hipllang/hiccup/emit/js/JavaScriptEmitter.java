package com.mattmerr.hipllang.hiccup.emit.js;

import com.mattmerr.hipllang.hiccup.Block;
import com.mattmerr.hipllang.hiccup.Expression;
import com.mattmerr.hipllang.hiccup.Program;
import java.io.PrintStream;

public class JavaScriptEmitter {

  private PrintStream out;
  private String leftPad = "        ";
  private int indentationLevel;

  public JavaScriptEmitter(PrintStream printStream) {
    this.out = printStream;
  }

  private void pushIndent() {
    indentationLevel += 1;
    if (leftPad.length() < indentationLevel * 2) {
      leftPad = leftPad + "  ";
    }
  }

  private void popIndent() {
    indentationLevel -= 1;
  }

  private void emitIndentation() {
    out.append(leftPad, 0, 2 * indentationLevel);
  }

  public void emitProgram(Program program) {
    emitBlockBody(program.getBlock());
  }

  public void emitBlockBody(Block block) {
    for (Expression exp : block.getExpressionList()) {
      emitIndentation();
      emitExpression(exp);
      out.println(';');
    }
  }

  public void emitExpression(Expression expression) {
    switch (expression.getValueCase()) {
      case NAME:
        out.print(expression.getName().getValue());
        break;

      case BLOCK:
        out.println("{");
        pushIndent();
        emitBlockBody(expression.getBlock());
        popIndent();
        out.println("}");
    }
  }

}
