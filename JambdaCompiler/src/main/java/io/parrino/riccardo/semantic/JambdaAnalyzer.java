package io.parrino.riccardo.semantic;

import io.parrino.riccardo.lexer.Token;
import io.parrino.riccardo.parser.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

public class JambdaAnalyzer {

    List<Node> ast;

    public void init(List<Node> ast) {
        this.ast = ast;
    }

    public void check() {
        testMainDefinition();
        testNoOverloading();
    }

    // main existence
    public void testMainDefinition() {
        for (Node node : ast) {
            if (node instanceof FuncDecl funcDecl) {
                if (funcDecl.getFunctionName().equals("main"))
                    return;
            }
        }
        throw new IllegalArgumentException("Main function not defined in your .jam file!");
    }

    // test every function has different name: no overloading is permitted
    public void testNoOverloading() {
        int funcDecls = 0;
        Set<String> funcDeclName = new HashSet<>();
        for (Node node : ast) {
            if (node instanceof FuncDecl funcDecl) {
                funcDecls++;
                funcDeclName.add(funcDecl.getFunctionName());
            }
        }
        if (funcDeclName.size() != funcDecls)
            throw new IllegalArgumentException("Function overloading not supported!");
    }

    // test every variable is defined before used
    // you can define the function after its application in a certain point of the program
    // no hoisting in variable declaration
    public void testVariableDeclaration() {
        for (Node node : ast) {

            if (node instanceof FuncDecl funcDecl) {
                SymbolTable st = createSymbolTableForFuncDecl(funcDecl);
            }

        }
    }

    // inheritedSymbolTable refers to the nearest parent scope Symbol Table
    private SymbolTable createSymbolTableForFuncDecl(FuncDecl funcDecl, SymbolTable inheritedSymbolTable) {return null;}

    // create a symbol table containing the only function declaration parameter
    private SymbolTable createSymbolTableForFuncDecl(FuncDecl funcDecl) {
        SymbolTable symbolTable = new SymbolTable();
        for (Field f : funcDecl.getParams()) {
            symbolTable.add(new STEntry(f.getName(), f.getType(), null));
        }
        return symbolTable;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private class STEntry {
        private String name;
        private Token type;
        private String value;
    }

    @Setter
    @Getter
    private class SymbolTable {
        Map<String, STEntry> entries;

        SymbolTable() {this.entries = new HashMap<>();}

        public boolean exists(String name) {
            return entries.get(name) != null;
        }

        public void add(STEntry stEntry) {
            if (exists(stEntry.getName()))
                throw new IllegalArgumentException("Variable name already defined!");
            entries.put(stEntry.getName(), stEntry);
        }
    }

    private Token inferTypeFromLiteral() {
        return Token.NUMERIC;
    }

    // TODO: verify matching return type function and return expression type
    // TODO: verify matching between function parameter and literal passed to the function application

}

