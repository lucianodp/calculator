package exceptions;

public class UnknownCharacterException extends RuntimeException {
    public UnknownCharacterException(char ch) {
        super("Found unknown character in input string: " + ch);
    }
}
