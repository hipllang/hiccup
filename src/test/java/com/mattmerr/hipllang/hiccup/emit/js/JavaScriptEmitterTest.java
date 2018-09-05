package com.mattmerr.hipllang.hiccup.emit.js;

import com.mattmerr.hipllang.hiccup.Program;
import com.mattmerr.hipllang.hiccup.parser.Parser;
import com.mattmerr.hipllang.hiccup.parser.TokenStream;
import java.io.IOException;
import org.junit.Test;

public class JavaScriptEmitterTest {

  @Test
  public void test() throws IOException {
    JavaScriptEmitter jse = new JavaScriptEmitter(System.out);
    Program prog = Parser.parseProgram(new TokenStream("hello; scope {world;};", "test"));
    jse.emitProgram(prog);
  }

}