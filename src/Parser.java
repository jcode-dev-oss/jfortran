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

class FieldList {
    Token openingToken;
    List<Field> fields;
    Token closingToken;

    FieldList(){}
    FieldList(Token openingToken, List<Token> fields, Token closingToken) {
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
        switch (this.tok) {
            case Token.FUNCTION:
                return this.parseFuncDecl();
            default:
                throw new IllegalArgumentException("Error occurred in parsing file: no valid token declaration found!");
        }
    }

    private Token expect(Token tok) {
        Token pos = this.tok;
        if (this.pos != tok)
            throw new IIllegalArgumentException("Expected " + tok + " token, but " + this.pos + " found!");

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

        switch (tok) {
            case Token.INT | Token.REAL | Token.LOGICAL | Token.CHAR:
                p.next();
                var name = this.parseIdent();
                return new Field(tok, name);
            default:
                throw new IllegalArgumentException("Please, fix you type in function declaration!");
        }
    }

    private List<Field> parseParameterList(Token name0, Token typ0, Toke closingToken, Boolean dddok) {
        List<Field> fields = new ArrayList<>();
        for (; this.tok != closingToken; ) {
            Field par = this.parseParamDecl();
            fields.add(par);
            if !(this.atComma()) {
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

        Token p = this.expect(token.FUNCTION);
        Token functionName = this.parseIdent();
        FieldList fieldList;
        if (this.tok == Token.LPAREN) {
            fieldList = this.parseTypeParameters();
        }
        Token rparen = this.expect(Token.RPAREN);

        Token semicolon = this.expect(Token.SEMICOLON);

        Token returnType = this.expect(List.of(Token.INT....);

        BlockStmt body;
        switch (this.tok) {
            case Token.LBRACE:
                body = this.parseBody();
            default:
                throw new IllegalArgumentException("You should define your function body in function: " + functionName.getValue()+"!");
        }

        var funcDecl = new FuncDecl();
        funcDecl.

        return null;
    }

}

public class Parser {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        var parser = new JambdaParser();
        jambdaParser.init(args[0]);
    }

}