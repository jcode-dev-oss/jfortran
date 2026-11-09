import java.util.Arrays;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

// Lexer written in the style of Go Lexer
enum Token {

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

class JambdaLexer {

    class Scanner {
        // immutable state
        File file;
        String dir;
        char[] source;
        String err;
        Integer mode;

        // scanning state
        char ch; // current character
        int offset; // character offset
        int rdOffset; // readning offset (position after current character=
        int lineOffset; // current line offset
        boolean insertSemi; // insert a semicolon before next newline

        boolean endPositionValid;
        int endPosition;

        public void next() {
            if (this.rdOffset < source.length){
                this.offset = this.rdOffset;
                this.ch = this.source[this.rdOffset];
                this.rdOffset += 1;
            } else {
                this.ch = 0; // end of file reached
                return;
            }
        }

        public void skipWhiteSpace() {
            while(this.ch == ' '
                || this.ch == '\t'
                || this.ch == '\n'
                || this.ch == '\r')
                this.next();
        }
    }

    private Scanner scanner;

    public void init(String filename) throws FileNotFoundException, IOException {
        this.scanner = new Scanner();
        scanner.file = new File(filename);
        scanner.dir = scanner.file.getPath();
        scanner.source = readAllLines(filename);
        scanner.err = null;
        scanner.mode = null;

        scanner.offset = 0;
        scanner.rdOffset = 0;
        scanner.lineOffset = 0;
        scanner.insertSemi = false;

        scanner.endPositionValid = false;
        scanner.endPosition = 0;

        this.scanner.next();
    }

    private char[] readAllLines(String filename) throws FileNotFoundException, IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();

            char[] source = new char[everything.length()];
            for (int i = 0; i < source.length; i++)
                source[i] = everything.charAt(i);
            return source;
        }
    }

    private boolean isLetter(char ch) {
        return ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'));
    }

    private boolean isDigit(char ch) {
        return (ch >= '0' && ch <= '9');
    }

    private String scanIdentifier() {
        int offs = this.scanner.offset;
        int rdOffs = offs;

        while(  this.scanner.source[rdOffs] >= 'a'
                && this.scanner.source[rdOffs] <= 'z'
                && this.scanner.source[rdOffs] >= 'A'
                && this.scanner.source[rdOffs] <= 'Z'
                && this.scanner.source[rdOffs] >= '0'
                && this.scanner.source[rdOffs] <= '9' ) {
            rdOffs = rdOffs + 1;
        }

        while(isLetter(this.scanner.ch) || isDigit(this.scanner.ch))
            this.scanner.next();

        return new String(Arrays.copyOfRange(this.scanner.source, offs, this.scanner.rdOffset-1));
    }

    private String scanNumber() {
        int offs = this.scanner.offset;
        int rdOffs = offs;

        while(  this.scanner.source[rdOffs] >= '0'
                && this.scanner.source[rdOffs] <= '9' ) {
            rdOffs = rdOffs + 1;
        }

        if ((this.scanner.source[rdOffs] >= 'a' && this.scanner.source[rdOffs] <= 'z')
            || (this.scanner.source[rdOffs] >= 'A' && this.scanner.source[rdOffs] <= 'Z'))
            throw new IllegalArgumentException("A variable name cannot start with a digit!");

        while(isLetter(this.scanner.ch) || isDigit(this.scanner.ch))
            this.scanner.next();

        return new String(Arrays.copyOfRange(this.scanner.source, offs, this.scanner.rdOffset-1));
    }

    public Token scan() {
        // start to read next token
        this.scanner.skipWhiteSpace();
        char ch = this.scanner.ch;
        if (this.isLetter(ch)) { // this is a literal
            String literal = scanIdentifier();
            if (literal.length() > 1) { // keyword are bigger than one-character literal, so we should verify it is a keyword match
                return switch (literal) {
                    case "if" -> Token.IF;
                    case "then" -> Token.THEN;
                    case "else" -> Token.ELSE;
                    case "fun" -> Token.FUN;
                    case "int" -> Token.INT;
                    case "char" -> Token.CHAR;
                    case "real" -> Token.REAL;
                    case "logical" -> Token.LOGICAL;
                    case "return" -> Token.RETURN;
                    default -> Token.literalWithValue(literal);
                };
            } else { // here keyword is one-character string, so it is definetly a variable name
                return Token.literalWithValue(literal);
            }
        } else if (this.isDigit(ch)) { // this is a number
            String numeric = scanNumber();
            return Token.numericWithValue(numeric);
        } else { // this is anything else
            this.scanner.next(); // always make progress!!!???
            switch (ch) {
                case '\n':
                    return Token.SPACE;
//                    TO BE DONE
//                    case '"':
//                        return Token.STRING;
                case ':':
                    return Token.COLON;
                case ',':
                    return Token.COMMA;
                case ';':
                    return Token.SEMICOLON;
                case '(':
                    return Token.LPAREN;
                case ')':
                    return Token.RPAREN;
                case '{':
                    return Token.LBRACK;
                case '}':
                    return Token.RBRACK;
                case '+':
                    return Token.ADD;
                case '-':
                    return Token.SUB;
                case '*':
                    return Token.MUL;
                case '/':
                    return Token.DIV;
                case '%':
                    return Token.REM;
                case '<':
                    if (this.scanner.ch == '=') {
                        this.scanner.next();
                        return Token.LEQ;
                    }
                    return Token.LESS;
                case '>':
                    if (this.scanner.ch == '=') {
                        this.scanner.next();
                        return Token.GEQ;
                    }
                    return Token.GTR;
                default:
                    if (this.scanner.rdOffset >= this.scanner.source.length) return null;
                    else throw new IllegalArgumentException("There is an error in the character program!");
            }
        }
    }

}

public class Lexer {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        JambdaLexer jambdaLexer = new JambdaLexer();
        jambdaLexer.init(args[0]);
        try {
            Token token;
            while( (token = jambdaLexer.scan()) != null ) {
                System.out.println(token + "\t:\t" + token.getValue());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}