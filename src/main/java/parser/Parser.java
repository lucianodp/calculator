package parser;

import exceptions.ParseException;
import scanner.Token;
import scanner.TokenType;

import java.util.List;
import java.util.function.Supplier;

import static scanner.TokenType.*;

/**
 * The Parser is responsible building a syntax tree from a list of scanned tokens. It also detects syntax errors and
 * reports them to the user.
 *
 * Associativity and precedence rules are encoded in the grammar bellow:
 *
 *          expression -> add
 *          add        -> mult  [ (+, -) mult ]*
 *          mult       -> exp   [ (*, /) exp  ]*
 *          exp        -> group [ ^ exp ]*
 *          group      -> NUMBER | ( expression )
 *
 * Parsing is done through a Recursive Descent Parser implementation.
 */
public class Parser {
    /**
     * Input list of scanned tokens
     */
    private final List<Token> tokens;

    /**
     * Current token
     */
    private int current;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.current = 0;
    }

    /**
     * @return the parsed expression
     */
    public Expression parse() {
        return add();
    }

    /**
     * add -> mult  [ (+, -) mult ]*
     */
    private Expression add() {
        return parseBinary(this::mult, this::mult, PLUS, MINUS);
    }

    /**
     * mult -> exp [ (*, /) exp  ]*
     */
    private Expression mult() {
        //
        return parseBinary(this::exp, this::exp, STAR, SLASH);
    }

    /**
     * exp -> group [ ^ exp ]*
     */
    private Expression exp() {
        return parseBinary(this::group, this::exp, EXP);
    }

    private Expression parseBinary(Supplier<Expression> initialExpression, Supplier<Expression> repeatingExpression, TokenType... types) {
        Expression expr = initialExpression.get();

        while (match(types)) {
            if (isAtEnd()) {
                throw new ParseException("Missing RHS of binary expression.");
            }
            expr = new Expression.Binary(expr, previous(), repeatingExpression.get());
        }

        return expr;
    }

    /**
     * group -> NUMBER | ( expression )
     */
    private Expression group() {
        if (match(NUMBER)) {
            return new Expression.NumberLiteral(previous());
        }

        if (match(LEFT_PAR)) {
            Expression expr = parse();

            if (!match(RIGHT_PAR)) {
                throw new ParseException("Reached end of input, but ) was not found.");
            }

            return new Expression.GroupingExpression(expr);
        }

        throw new ParseException("Expected number or open parenthesis, found " + peek().getLexeme());
    }

    private boolean isAtEnd() {
        return current >= tokens.size();
    }

    private Token advance() {
        return tokens.get(current++);
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd())
            return false;

        return peek().getType() == type;
    }
}
