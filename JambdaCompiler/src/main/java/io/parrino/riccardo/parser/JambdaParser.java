package io.parrino.riccardo.parser;

import io.parrino.riccardo.lexer.JambdaLexer;
import io.parrino.riccardo.lexer.Token;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class JambdaParser {

    JambdaLexer jambdaLexer;
    Token tok;
    String lit;

    public JambdaParser init(String filename) throws FileNotFoundException, IOException {
        this.jambdaLexer = new JambdaLexer();
        jambdaLexer.init(filename);
        this.next0();
        return this;
    }

    private Token expect(Token tok) {
        Token pos = this.tok;
        if (pos != tok)
            throw new IllegalArgumentException("Expected " + tok + " token, but " + pos + " found!");

        this.next0();
        return pos;
    }

    private Token expect(EnumSet<Token> admittedToken) {
        Token pos = this.tok;
        if (!admittedToken.contains(pos))
            throw new IllegalArgumentException("Expected " + tok + " token, but " + pos + " found!");

        this.next0();
        return pos;
    }

    private void next0() {
        this.tok = this.jambdaLexer.scan();
        if ( tok != null )
            this.lit = this.tok.getValue();
    }

    private BlockStmt parseFuncBody() {
        List<Expr> stmts = this.parseExprList();
        return new BlockStmt(stmts);
    }

    private List<Expr> parseExprList() {
        List<Expr> exprs = new ArrayList<>();
        for (; this.tok != Token.RBRACK; ) {
            Expr expr = this.parseExpr();
            exprs.add(expr);
            if (!this.atSemicolon()) {
                break;
            }
            this.next0();
        }
        return exprs;
    }

    private Expr parseExpr() {
        switch (this.tok) {
            case Token.RETURN:
                return this.parseReturnExpression();
            case Token.LITERAL:
                return this.parseFuncApp();
            default:
                throw new IllegalArgumentException("Please create a valid function block!");
        }
    }

    private Expr parseReturnExpression() {
        Token returnToken = this.expect(Token.RETURN);
        Expr returnBlock = this.parseReturnBlock();
        return new ReturnExpr(returnToken, returnBlock);
    }

    private Expr parseReturnBlock() {
        return this.parseFuncApp();
    }

    private List<Field> parseFieldList() {
        List<Field> fields = new ArrayList<>();
        for (; this.tok != Token.RPAREN; ) {
            Field field = this.parseFieldDecl();
            fields.add(field);
            if (!this.atComma()) {
                break;
            }
            this.next0();
        }
        return fields;
    }

    private Field parseFieldDecl() {
        Token type = this.expect(EnumSet.of(Token.INT, Token.REAL, Token.CHAR, Token.LOGICAL));
        String name = this.expect(Token.LITERAL).getValue();
        return new Field(type, name);
    }

    private Node parseFuncDecl() {
        // will return
        // FUN NAME_OF_THE_FUNC ( PARAMETER_LIST ) { BODY }

        Token fun = this.expect(Token.FUN);
        String functionName = this.expect(Token.LITERAL).getValue();
        Token lparen = this.expect(Token.LPAREN);
        List<Field> fields = this.parseFieldList();
        Token rparen = this.expect(Token.RPAREN);
        Token colon = this.expect(Token.COLON);
        Token returnType = this.expect(EnumSet.of(Token.INT, Token.REAL, Token.CHAR, Token.LOGICAL, Token.VOID, Token.STRING));
        Token lbrack = this.expect(Token.LBRACK);
        BlockStmt body = this.parseFuncBody();
        Token rbrack = this.expect(Token.RBRACK);

        return new FuncDecl(
                fun,
                functionName,
                lparen,
                fields,
                rparen,
                colon,
                returnType,
                lbrack,
                body,
                rbrack
        );
    }

    private Expr parseFuncAppInParameterWithGivenName(String functionName) {
        Token lparen = this.expect(Token.LPAREN);
        List<Param> params = this.parseParamList();
        Token rparen = this.expect(Token.RPAREN);

        FuncApp funcApp = new FuncApp(
                functionName,
                lparen,
                params,
                rparen,
                null
        );

        return funcApp;
    }

    private Expr parseFuncAppWithGivenName(String functionName) {
        Token lparen = this.expect(Token.LPAREN);
        List<Param> params = this.parseParamList();
        Token rparen = this.expect(Token.RPAREN);
        Token semicolon = this.expect(Token.SEMICOLON);

        FuncApp funcApp = new FuncApp(
                functionName,
                lparen,
                params,
                rparen,
                semicolon
        );

        return funcApp;
    }

    private Expr parseFuncApp() {
        // will return
        // what to do with space?
        // NAME_OF_THE_FUNC ( PARAMETER_LIST );
        String functionName = this.expect(Token.LITERAL).getValue();
        Token lparen = this.expect(Token.LPAREN);
        List<Param> params = this.parseParamList();
        Token rparen = this.expect(Token.RPAREN);
        Token semicolon = this.expect(Token.SEMICOLON);

        FuncApp funcApp = new FuncApp(
                functionName,
                lparen,
                params,
                rparen,
                semicolon
        );

        return funcApp;
    }

    private List<Param> parseParamList() {
        List<Param> params = new ArrayList<>();
        for (; this.tok != Token.RPAREN; ) {
            Param par = this.parseParamDecl();
            params.add(par);
            if (!this.atComma()) {
                break;
            }
            this.next0();
        }
        return params;
    }

    private Param parseParamDecl() {
        var tok = this.tok;
        switch(tok) {
            case Token.LITERAL:
                this.next0();
                String literal = tok.getValue();
                if (this.tok == Token.LPAREN) {
                    Expr funcApp = this.parseFuncAppInParameterWithGivenName(literal);
                    return new Param(null, null, funcApp);
                } else {
                    return new Param(literal, null, null);
                }
            case Token.NUMERIC:
                this.next0();
                return new Param(null, tok, null);
            default:
                throw new IllegalArgumentException("Please, fix your parameters function invocation!");
        }
    }

    private boolean atComma() {
        return this.tok == Token.COMMA;
    }

    private boolean atSemicolon() {
        return this.tok == Token.SEMICOLON;
    }

    private void skipSpace() {
        for (; this.tok == Token.SPACE; )
            this.next0();
    }

    // returns the next node parsed from the file
    public Node next() {

        this.skipSpace();

        switch (this.tok) {
            case Token.FUN:
                return this.parseFuncDecl();
            case Token.LITERAL:
                return this.parseFuncApp();
            default:
                throw new IllegalArgumentException("Error in file input or reached EOF!");
        }
    }

    public List<Node> ast() {
        List<Node> ast = new ArrayList<>();
        int i = 0;
        while (i < 4) {
            var node = this.next();
            ast.add(node);
            i++;
        }
        return ast;
    }

}
