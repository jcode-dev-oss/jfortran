package io.parrino.riccardo.parser;

import io.parrino.riccardo.lexer.Token;

public class Param {
    String literal;
    Token numeric;
    Param(){}
    Param(String literal, Token numeric){
        this.literal = literal;
        this.numeric = numeric;
    }
    public String toString() {
        if (this.literal != null)
            return "Literal: " + this.literal;
        else if (this.numeric != null)
            return this.numeric.toString() + ": " + this.numeric.getValue();
        else
            return null;
    }
}