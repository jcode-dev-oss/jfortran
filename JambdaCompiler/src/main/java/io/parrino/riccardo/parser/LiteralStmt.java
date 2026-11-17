package io.parrino.riccardo.parser;

public class LiteralStmt extends Expr{
    String name;

    LiteralStmt(){}
    LiteralStmt(String name){this.name = name;}

    @Override
    public byte[] bytecodeGenerator() {
        return new byte[0];
    }
}
