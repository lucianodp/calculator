package scanner;

import exceptions.UnknownCharacterException;

import java.util.ArrayList;
import java.util.List;

import static scanner.TokenType.*;

/**
 * scanner.Scanner class for the calculator. Parses the input string, identifying valid tokens.
 */
public class Scanner {
    /**
     * Input string to be parsed
     */
    private final String input;

    /**
     * Starting position of the next lexeme
     */
    private int start;

    /**
     * Current character position to be consumed in the string
     */
    private int current;

    /**
     * List of scanned tokens
     */
    private final List<Token> scannedTokens;

    public Scanner(String input) {
        this.input = input;
        this.start = 0;
        this.current = 0;
        this.scannedTokens = new ArrayList<>();
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        return scannedTokens;
    }

    private void scanToken() {
        char ch = advance();

        switch (ch) {
            // skip all whitespace characters
            case ' ':
            case '\t':
            case '\r':
            case '\n':
                break;

            // parenthesis
            case '(':
                addToken(LEFT_PAR); break;
            case ')':
                addToken(RIGHT_PAR); break;

            // operator signs
            case '+':
                addToken(PLUS); break;
            case '-':
                addToken(MINUS); break;
            case '*':
                addToken(STAR); break;
            case '/':
                addToken(SLASH); break;
            case '^':
                addToken(EXP); break;
            default:
                if (Character.isDigit(ch)) {
                    addNumberToken();
                }
                else {
                    throw new UnknownCharacterException(peek());
                }
        }
    }

    private boolean isAtEnd() {
        return current >= input.length();
    }

    private char advance() {
        return input.charAt(current++);
    }

    private char peek() {
        return input.charAt(current);
    }

    private void addNumberToken() {
        while (!isAtEnd() && Character.isDigit(peek())) {
            advance();
        }
        addToken(NUMBER, Integer.parseInt(getCurrentSubstring()));
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object value) {
        scannedTokens.add(newToken(type, value));
    }

    private Token newToken(TokenType type, Object value) {
        return new Token(type, getCurrentSubstring(), value);
    }

    private String getCurrentSubstring() {
        return input.substring(start, current);
    }
}
