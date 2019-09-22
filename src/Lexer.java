import java.util.ArrayList;
import java.util.List;

class Lexer {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    Lexer(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(':
                addToken(TokenType.LEFT_PAREN);
                break;
            case ')':
                addToken(TokenType.RIGHT_PAREN);
                break;
            case '-':
                addToken(TokenType.MINUS);
                break;
            case '+':
                addToken(TokenType.PLUS);
                break;
            case '/':
                addToken(TokenType.SLASH);
                break;
            case '*':
                addToken(TokenType.STAR);
                break;
            case '=':
                addToken(TokenType.EQUAL);
                break;
            case '<':
                addToken(TokenType.LESS);
                break;
            case '>':
                addToken(TokenType.GREATER);
                break;
            default:
                if (isDigit(c)) {
                    number();
                }
        }
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';

    }

    private void number() {
        while (isDigit(peek())) advance();

        if (peek() == '.' && isDigit(peekNext())) {
            advance();

            while (isDigit(peek())) advance();
        }
        addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private char peek() {
        if (isAtEnd()) return 0;
        return source.charAt(current);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));

    }

    private char peekNext() {
        if (current + 1 >= source.length()) return 0;
        return source.charAt(current + 1);
    }

    private char advance() {
        current++;
        return source.charAt(current - 1);
    }
}
