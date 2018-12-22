# calculator
A simple java calculator implemented through a Recursive Descent Parser

This simple command-line calculator lets you evaluate simple mathematical expressions involving +, -, *, / and ^ operators.
It also supports parenthised expressions, and any syntax errors are reported back to the user.

This calculator's implementation consists of two main parts: a Scanner class which parses the input string into a series of 
tokens, and a Parser class which builds an Abstract Syntax Tree from the previous token stream. Care was taken all usual operator
precedence and assossiativity rules are valid.

References:
  [1] http://www.craftinginterpreters.com
