package io.parrino.riccardo.lexer;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class JambdaLexerTest {

    @Test
    public void initJambdaLexerTest() throws IOException {
        JambdaLexer jambdaLexer = new JambdaLexer();
        jambdaLexer.init("D:\\repos\\bytecode\\jambda\\JambdaCompiler\\src\\test\\java\\resources\\example.jam");
    }

}
