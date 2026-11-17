package io.parrino.riccardo.generator;

import io.parrino.riccardo.parser.JambdaParser;
import io.parrino.riccardo.parser.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JambdaGeneratorTest {

    @Test
    void simpleFuncDeclTest() throws IOException {
        var jambdaParser = new JambdaParser();
        jambdaParser.init("D:\\repos\\bytecode\\jambda\\JambdaCompiler\\src\\test\\java\\resources\\simpleFuncDecl.jam");
        List<Node> ast = jambdaParser.ast();

        JambdaGenerator jGenerator = new JambdaGenerator();
        jGenerator.init(ast);

        byte[] bytecode = jGenerator.generate();

        Assertions.assertNotNull(bytecode);

        Path outputPath = Paths.get("D:\\repos\\bytecode\\jambda\\JambdaCompiler\\src\\test\\java\\resources\\Hello.class");
        Files.write(outputPath, bytecode);
    }

}
