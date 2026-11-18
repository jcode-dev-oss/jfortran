package io.parrino.riccardo;

import io.parrino.riccardo.generator.JambdaGenerator;
import io.parrino.riccardo.parser.JambdaParser;
import io.parrino.riccardo.parser.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        var jambdaParser = new JambdaParser();
        jambdaParser.init("D:\\repos\\bytecode\\jfortran\\JambdaCompiler\\src\\main\\resources\\simpleFuncDecl.jam");
        List<Node> ast = jambdaParser.ast();

        JambdaGenerator jGenerator = new JambdaGenerator();
        jGenerator.init(ast);

        byte[] bytecode = jGenerator.generate();

        Path outputPath = Paths.get("D:\\repos\\bytecode\\jfortran\\JambdaCompiler\\src\\main\\resources\\Hello.class");
        Files.write(outputPath, bytecode);
    }
}