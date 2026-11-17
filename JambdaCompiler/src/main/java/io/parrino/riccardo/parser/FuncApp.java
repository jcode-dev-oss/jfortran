package io.parrino.riccardo.parser;

import io.parrino.riccardo.lexer.Token;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class FuncApp extends Expr {
    String functionName;
    Token lparen;
    List<Param> params;
    Token rparen;
    Token semicolon;

    FuncApp(){}
    FuncApp(
            String functionName,
            Token lparen,
            List<Param> params,
            Token rparen,
            Token semicolon
    ){
        this.functionName = functionName;
        this.lparen = lparen;
        this.params = params;
        this.rparen = rparen;
        this.semicolon = semicolon;
    }

    @Override
    public byte[] bytecodeGenerator() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        cw.visit(
                V21,
                ACC_PUBLIC,
                "Function",
                null,
                "java/lang/Object",
                null
        );

        // create constructor
        MethodVisitor mv = cw.visitMethod(
                ACC_PUBLIC,
                "<init>",
                "()V",
                null,
                null
        );

        mv.visitCode();

        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(
                INVOKESPECIAL,
                "java/lang/Object",
                "<init>",
                "()V",
                false
        );

        mv.visitInsn(RETURN);
        mv.visitMaxs(0,0);
        mv.visitEnd();

        return null;
    }

    @Override
    public String toString() {
        return this.functionName
                + this.lparen.getValue() + " "
                + this.params.stream().map(p -> p.toString()).map(p -> p+", ").reduce( (a,b) -> a+b ) + " "
                + this.rparen.getValue() + " "
                + this.semicolon.getValue();
    }

//    public void myFunction(int a, int b) {
//        myFunction2(a,b);
//    }
}
