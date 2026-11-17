package io.parrino.riccardo.parser;

import io.parrino.riccardo.lexer.Token;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.RETURN;

@Getter
@Setter
public class FuncDecl extends Stmt {
    Token fun;
    String functionName;
    Token lparen;
    List<Field> params;
    Token rparen;
    Token colon;
    Token returnType;
    Token lbrack;
    BlockStmt blockStmt;
    Token rbrack;

    @Override
    public String toString() {
        return fun.getValue() + " "
                + functionName + " "
                + lparen.getValue() + " "
                + params.stream().map(p -> p.type.getValue() + " " + p.name).reduce((a,b) -> a + ", " + b) +  " "
                + rparen.getValue() + " "
                + colon.getValue() + " "
                + returnType.getValue() + " "
                + lbrack.getValue() + " "
                + " " // blockstmt goes here TODO
                + rbrack.getValue();
    }

    FuncDecl(){}
    FuncDecl(
            Token fun,
            String functionName,
            Token lparen,
            List<Field> params,
            Token rparen,
            Token colon,
            Token returnType,
            Token lbrack,
            BlockStmt blockStmt,
            Token rbrack
    ) {
        this.fun = fun;
        this.functionName = functionName;
        this.lparen = lparen;
        this.params = params;
        this.rparen = rparen;
        this.colon = colon;
        this.returnType = returnType;
        this.lbrack = lbrack;
        this.blockStmt = blockStmt;
        this.rbrack = rbrack;
    }

    @Override
    public byte[] bytecodeGenerator() {
        return null;
    }
}