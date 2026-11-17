package io.parrino.riccardo.parser;

import io.parrino.riccardo.lexer.Token;

public class ReturnExpr extends Expr {
    Token returnToken;
    Expr returnExpr;

    ReturnExpr(){}
    ReturnExpr(Token returnToken, Expr returnExpr){
        this.returnToken = returnToken;
        this.returnExpr = returnExpr;
    }

    @Override
    public String toString() {
        return returnToken.getValue() + " "
                + returnExpr.toString();
    }

    @Override
    public byte[] bytecodeGenerator() {
        return new byte[0];
    }
}
