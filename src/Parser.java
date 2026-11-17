import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/*
* Only function application
* No assignment
* No function composition
*
*
* */

/*
FUNC_DEF -> EPS
    | FUNCTION LITERAL ORPAR PAR_DEF CRPAR OBRA FUNC_APP CBRA FUNC_DEF;
FUNC_APP -> EPS
    | LITERAL ORPAR PAR_APP CRPAR SEMICOLON FUNC_APP;

PAR_DEF -> PAR_DEF COMMA TYPE LITERAL
    | TYPE LITERAL
    | EPS;
PAR_APP -> PAR_APP COMMA LITERAL
    | PAR_APP COMMA VALUE
    | LITERAL
    | VALUE
    | EPS;

TYPE -> INTEGER
    | REAL
    | LOGICAL
    | CHARACTER
    | STRING;
*/

abstract class Node {
    abstract void bytecodeGenerator();
}

abstract class Decl extends Node {
}

abstract class Expr extends Node {
}

abstract class Stmt extends Node {
}

class EmptyStmt extends Stmt {
    @Override
    void bytecodeGenerator(){}
}

class BlockStmt extends Stmt{
    Token lbracket;
    List<Stmt> stmts;
    Token rbracket;

    BlockStmt(){}
    BlockStmt(Token lbracket,List<Stmt> stmts,Token rbracket){
        this.lbracket = lbracket;
        this.stmts = stmts;
        this.rbracket = rbracket;
    }

    @Override
    void bytecodeGenerator(){}
}

class ReturnStmt extends Stmt {
    Token tok;
    Expr expr;

    ReturnStmt(){}
    ReturnStmt(Token tok, Expr expr){
        this.tok = tok;
        this.expr = expr;
    }

    @Override
    void bytecodeGenerator(){}
}

class ParenExpr extends Expr {
    Token lparen;
    Expr x;
    Token rparen;

    ParenExpr(){}
    ParenExpr(Token lparen, Expr x, Token rparen){
        this.lparen = lparen;
        this.x = x;
        this.rparen = rparen;
    }
}

class FieldList {
    Token openingToken;
    List<Field> fields;
    Token closingToken;

    FieldList(){}
    FieldList(Token openingToken, List<Field> fields, Token closingToken) {
        this.openingToken = openingToken;
        this.fields = fields;
        this.closingToken = closingToken;
    }
}

class FuncDecl extends Decl {
    List<Field> params;
    Token returnType;
    Token functionName;
    BlockStmt body;

    @Override
    void bytecodeGenerator(){
        return;
    }
}

class File {
    // CommentGroup doc // associated documentation; or nul
    // Token package // position of "package" keyword
    // Ident name // package name
    Decl[] decls;

    // Token Filestart; // start and end of entire file
    // Token FileEnd;
    // Scope scope // package scope (this file only)
    // Import[] imports; // imports in this file
    // Comment[] comments;
    String javaVersion; // java version of this file
}

class FuncDecl {
    // String comment; TODO
    String functionName;
    List<Token> parameterList;
    Token returnType;
    List<Token> body;
}

class Field {
    // String doc;
    // String commentGroup;
    Token name;
    Token types;
    // String tag;

    Field(){}
    Field(Token types, Token name) {
        this.name = name;
        this.types = types;
    }
}

// The parser structure holds the parser's internal state.
class JambdaParser {

//    File file;
//    List<Integer> errors;
    JambdaLexer lexer;

//    int mode;
//    boolean trace;
//    int indent;

    // comments TODO
    // CommentGroup comments;
    // CommentGroup leadComment;
    // CommentGroup lineComment;
    // boolean top;
    // String goVersion;

    // Next token
    // Token pos; // token position
    Token tok; // one token look-ahead
    String lit; // token literal

    // Error recovery
    // Token syncPos;
    // int syncCnt;

    // Non-syntactic parser control
    // int exprLev;
    // boolean inRhs;

    // List of imports TODO
    // ImportSpec[] imports

    // nestLev is used to track and limit the recursion depth
    // during parsing
    int nestLev;

    public JambdaParser init(String filename) {
        this.lexer = new JambdaLexer();
        jambdaLexer.init(filename);
        this.tok = null;
        this.lit = null;
    }

    public Decl parseDecl() {
        Token pos = this.tok;
        switch (this.tok) {
            case Token.FUN:
                return this.parseFuncDecl();
            default:
                throw new IllegalArgumentException("Error occurred in parsing file: no valid token declaration found!");
        }
    }

    private Token expect(Token tok) {
        Token pos = this.tok;
        if (this.pos != tok)
            throw new IllegalArgumentException("Expected " + tok + " token, but " + this.pos + " found!");

        this.next();
        return pos;
    }

    private void next0() {
        this.pos = this.lexer.scan();
        this.lit = this.pos.getValue();
    }

    private Token parseIdent() {
        String name = null;
        if (this.tok == Token.LITERAL) {
            name = this.tok.getValue();
        } else {
            this.expect(Token.LITERAL); // use expect() error handling
        }
        return Token.literalWithValue(name);
    }

    private Field parseParamDecl() {
        var tok = this.tok;
        Token name;
        switch (tok) {
            case Token.INT:
                this.next0();
                name = this.parseIdent();
                return new Field(tok, name);
            case Token.REAL:
                name = this.parseIdent();
                return new Field(tok, name);
            case Token.LOGICAL:
                name = this.parseIdent();
                return new Field(tok, name);
            case Token.CHAR:
                name = this.parseIdent();
                return new Field(tok, name);
            default:
                throw new IllegalArgumentException("Please, fix you type in function declaration!");
        }
    }

    private List<Field> parseParameterList(Token name0, Token typ0, Token closingToken, Boolean dddok) {
        List<Field> fields = new ArrayList<>();
        for (; this.tok != closingToken; ) {
            Field par = this.parseParamDecl();
            fields.add(par);
            if (!this.atComma()) {
                break;
            }
            this.next0();
        }
        return fields;
    }

    private boolean atComma() {
        return this.tok == Token.COMMA;
    }

    private FieldList parseTypeParameters() {

        List<Field> list = new ArrayList<>();
        Token lparen = this.expect(Token.LPAREN);
        if(this.tok != Token.RPAREN)
            list = this.parseParameterList(null, null, Token.RPAREN, false);
        Token rparen = this.expect(Token.RPAREN);

        if (list.size() == 0)
            throw IllegalArgumentException("Put parameters correctly in function definition!");

        return new FieldList(lparen, list, rparen);
    }

    private FuncDecl parseFuncDecl() {

        Token p = this.expect(Token.FUN);
        Token functionName = this.parseIdent();
        FieldList fieldList;
        if (this.tok == Token.LPAREN) {
            fieldList = this.parseTypeParameters();
        }
        Token rparen = this.expect(Token.RPAREN);

        Token semicolon = this.expect(Token.SEMICOLON);

        Token returnType = this.expect(Token.INT); // to be generalized to one or more types

        BlockStmt body;
        switch (this.tok) {
            case Token.LBRACK:
                body = this.parseBody();
            default:
                throw new IllegalArgumentException("You should define your function body in function: " + functionName.getValue()+"!");
        }

        var funcDecl = new FuncDecl();
        funcDecl.params = fieldList.fields;
        funcDecl.returnType = returnType;
        funcDecl.body = body;
        funcDecl.functionName = functionName;

        return funcDecl;
    }

    private BlockStmt parseBody () {
        var lbrack = this.expect(Token.LBRACK);
        var list = this.parseStmtList();
        var rbrack = this.expect(Token.RBRACK);
        return new BlockStmt(
                lbrack,
                list,
                rbrack);
    }

    private List<Stmt> parseStmtList() {
        List<Stmt> stmts = new ArrayList<>();
        while (this.tok != Token.RBRACK) {
            stmts.add(parseStmt());
        }
        return stmts;
    }

    private Stmt parseStmt() {
        switch (this.tok) {
            case Token.LITERAL:
                return this.parseSimpleStmt();
            case Token.RETURN:
                return this.parseReturnStmt();
            case Token.RBRACK:
                return new EmptyStmt();
        }
    }

    private Stmt parseReturnStmt() {
        this.expect(Token.RETURN);
        Expr e;
        if (this.tok != Token.SEMICOLON && this.tok != Token.RBRACK) {
            e = this.parseList();
        }
        return new ReturnStmt(this.tok, e);
    }

    private List<Expr> parseSimpleStmt() {
        var s = this.parseList();
        return s;
    }
    
    private List<Expr> parseList() {
        return this.parseExprList();
    }

    private List<Expr> parseExprList() {
        List<Expr> exprList = new ArrayList<>();
        exprList.add(this.parseExpr());
        while(this.tok == Token.COMMA) {
            this.next0();
            exprList.add(this.parseExpr());
        }
        return exprList;
    }

    private Expr parseExpr() {
        return this.parseBinaryExpr(null, 1);
    }

    private Expr parseBinaryExpr(Expr x, int precedence) {
        if (x == null)
            x = this.parseUnaryExpr();

        return x;
    }

    private Expr parseUnaryExpr() {
        return this.parsePrimaryExpr(null);
    }

    private Expr parsePrimaryExpr(Expr x) {
        if (x == null) {
            x = parseOperand();
        }
        return x;
    }

    private Expr parseOperand() {
        switch (this.tok) {
            case Token.LITERAL:
                return this.parseIdent();
            case Token.LPAREN:
                var lparen = this.tok;
                this.next0();
                var x = this.parseRhs();
                var rparen = this.expect(Token.RPAREN);
                return new ParenExpr(
                        lparen,
                        x,
                        rparen
                );
            case Token.FUN:
                return this.parseFuncTypeOrLit();
        }
    }

    // TODO
    private Expr parseFuncTypeOrLit(){
        return null;
    }

    private CallExpr parseCallOrConversion(Expr fun) {
        var lparen = this.expect(Token.LPAREN);
        List<Expr> list = new ArrayList<>();
        while (this.tok != Token.RPAREN) {
            list.add(this.parseRhs());
        }
    }

    private Expr parseRhs() {
        return this.parseExpr();
    }

}

public class Parser {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        var parser = new JambdaParser();
        jambdaParser.init(args[0]);
    }

}