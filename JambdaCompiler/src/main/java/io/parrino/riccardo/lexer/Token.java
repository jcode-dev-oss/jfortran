package io.parrino.riccardo.lexer;

public enum Token {

    ADD("+"),
    SUB("-"),
    MUL("*"),
    DIV("/"),
    REM("%"),

    AND("and"),
    OR("or"),

    EQL("=="),
    NEQ("!="),
    LEQ("<="),
    GEQ(">="),
    LESS("<"),
    GTR(">"),

    LPAREN("("),
    LBRACK("{"),

    RPAREN(")"),
    RBRACK("}"),

    COMMA(","),
    COLON(":"),
    SEMICOLON(";"),

    INT("integer"),
    REAL("real"),
    CHAR("character"),
    LOGICAL("logical"),
    VOID("void"),
    LITERAL("literal"),
    NUMERIC("numeric"),
    STRING(""),

    FUN("fun"),
    IF("if"),
    ELSE("else"),
    THEN("then"),
    RETURN("return"),

    SPACE("");

    private String value;

    Token(String value){
        this.value = value;
    }

    public static Token literalWithValue(String value) {
        Token literal = Token.LITERAL;
        literal.setValue(value);
        return literal;
    }

    public static Token numericWithValue(String value) {
        Token numeric = Token.NUMERIC;
        numeric.setValue(value);
        return numeric;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
