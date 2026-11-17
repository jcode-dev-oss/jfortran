package io.parrino.riccardo.parser;

import io.parrino.riccardo.lexer.Token;

public class Param extends Expr {
    String literal;
    Token numeric;
    Expr funcApp;

    Param(){}
    Param(String literal, Token numeric, Expr funcApp){
        this.literal = literal;
        this.numeric = numeric;
        this.funcApp = funcApp;
    }

    public String toString() {
        if (this.literal != null)
            return "Literal: " + this.literal;
        else if (this.numeric != null)
            return this.numeric.toString() + ": " + this.numeric.getValue();
        else
            return null;
    }

    @Override
    public byte[] bytecodeGenerator() {
        return new byte[0];
    }
}