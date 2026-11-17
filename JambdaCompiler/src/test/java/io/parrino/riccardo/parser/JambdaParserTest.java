package io.parrino.riccardo.parser;

import io.parrino.riccardo.lexer.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class JambdaParserTest {

    @Test
    void initJambdaParserTest () throws IOException {
        var jambdaParser = new JambdaParser();
        jambdaParser.init("D:\\repos\\bytecode\\jambda\\JambdaCompiler\\src\\test\\java\\resources\\example.jam");
    }

    @Test
    void exampleOnlyFuncAppFileParseTest() throws IOException {
        var jambdaParser = new JambdaParser();
        jambdaParser.init("D:\\repos\\bytecode\\jambda\\JambdaCompiler\\src\\test\\java\\resources\\exampleOnlyFuncApp.jam");
        var node = (FuncApp)jambdaParser.next();

        Assertions.assertEquals("myFunction", node.functionName);
        Assertions.assertEquals("a", node.params.getFirst().literal);
        Assertions.assertEquals("b", node.params.get(1).literal);
        Assertions.assertEquals("19", node.params.get(2).numeric.getValue());
    }

    @Test
    void exampleOnlyFuncDeclFileParseTest() throws IOException {
        var jambdaParser = new JambdaParser();
        jambdaParser.init("D:\\repos\\bytecode\\jambda\\JambdaCompiler\\src\\test\\java\\resources\\exampleOnlyFuncDecl.jam");
        var node = (FuncDecl)jambdaParser.next();

        System.out.println(node.params);
        // System.out.println(node);

        Assertions.assertNotNull(node.fun);
        Assertions.assertEquals("myFunction1", node.functionName);
        Assertions.assertNotNull(node.lparen);

        Assertions.assertNotNull(node.params);
        Assertions.assertEquals("a", node.params.getFirst().name);
        Assertions.assertEquals("b", node.params.get(1).name);

        Assertions.assertNotNull(node.rparen);
        Assertions.assertNotNull(node.colon);

        Assertions.assertNotNull(node.returnType);
        Assertions.assertEquals(Token.INT.getValue(), node.returnType.getValue());

        Assertions.assertNotNull(node.lbrack);
        Assertions.assertNotNull(node.blockStmt);
        Assertions.assertNotNull(node.rbrack);
    }

    @Test
    void exampleOnlyFuncDeclFileParseTest2() throws IOException {
        var jambdaParser = new JambdaParser();
        jambdaParser.init("D:\\repos\\bytecode\\jambda\\JambdaCompiler\\src\\test\\java\\resources\\exampleOnlyFuncDecl.jam");
        var node = (FuncDecl) jambdaParser.next();
        node = (FuncDecl) jambdaParser.next();
        node = (FuncDecl) jambdaParser.next();
    }

//    @Test
//    void exampleOnlyFuncDeclFileParseTest3() throws IOException {
//        var jambdaParser = new JambdaParser();
//        jambdaParser.init("D:\\repos\\bytecode\\jambda\\JambdaCompiler\\src\\test\\java\\resources\\exampleOnlyFuncDecl.jam");
//        var node = (FuncDecl) jambdaParser.next();
//        node = (FuncDecl) jambdaParser.next();
//        node = (FuncDecl) jambdaParser.next();
//        node = (FuncDecl) jambdaParser.next();
//        System.out.println(node.blockStmt);
//    }

}
