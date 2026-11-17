package io.parrino.riccardo.parser;

import io.parrino.riccardo.lexer.Token;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Field {
    Token type;
    String name;
    Field(){}
    Field(Token type, String name){
        this.type = type;
        this.name = name;
    }

    @Override
    public String toString() {
        return type.getValue() + " "
                + name;
    }
}