package parser;

import exceptions.ParseException;
import scanner.Token;

/**
 * This class encodes the syntax tree of our grammar. Each Expression is capable of evaluating the partial result of the
 * subtree rooted in itself.
 *
 * Example: (1 + 2) * 3 ^ 4
 *
 *          *
 *        /   \
 *       +     ^
 *      / \   / \
 *     1  2  3  4
 */
public interface Expression {

    /**
     * @return the value of the subtree rooted in itself.
     */
    int evaluate();

    /**
     * Binary expressions. These are nodes in the syntax tree with two children.
     */
    class Binary implements Expression {
        final Expression left, right;
        final Token operand;

        public Binary(Expression left, Token operand, Expression right) {
            this.left = left;
            this.right = right;
            this.operand = operand;
        }

        @Override
        public int evaluate() {
            int leftValue = left.evaluate();
            int rightValue = right.evaluate();

            switch (operand.getType()) {
                case PLUS:
                    return leftValue + rightValue;
                case MINUS:
                    return leftValue - rightValue;
                case STAR:
                    return leftValue * rightValue;
                case SLASH:
                    return leftValue / rightValue;
                case EXP:
                    return (int) Math.pow(leftValue, rightValue);
                default:
                    throw new ParseException("Unknown binary operator: " + operand);
            }
        }

        @Override
        public String toString() {
            return "[ " + left.toString() + ' ' + operand.getType() + ' ' + right.toString() + " ]";
        }
    }

    /**
     * Parenthesis expressions
     */
    class GroupingExpression implements Expression {
        private final Expression expression;

        public GroupingExpression(Expression expression) {
            this.expression = expression;
        }

        @Override
        public int evaluate() {
            return expression.evaluate();
        }

        @Override
        public String toString() {
            return '(' + expression.toString() + ')';
        }
    }

    /**
     * Number literal. These correspond to the leaves of the syntax tree.
     */
    class NumberLiteral implements Expression {
        final Token numberToken;

        public NumberLiteral(Token token) {
            this.numberToken = token;
        }

        @Override
        public int evaluate() {
            return (int) numberToken.getValue();
        }

        @Override
        public String toString() {
            return ((Integer) numberToken.getValue()).toString();
        }
    }
}
