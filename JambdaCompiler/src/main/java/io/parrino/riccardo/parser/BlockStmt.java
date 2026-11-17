package io.parrino.riccardo.parser;

import lombok.Getter;
import lombok.Setter;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

@Getter
@Setter
public class BlockStmt extends Node {
    List<Expr> stmts;

    BlockStmt(){}
    BlockStmt(List<Expr> stmts) {
        this.stmts = stmts;
    }

    public void bytecodeGeneration(MethodVisitor mv) {

    }

    @Override
    public byte[] bytecodeGenerator() {
        return null;
    }
}
