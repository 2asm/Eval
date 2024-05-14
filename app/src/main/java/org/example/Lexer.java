package org.example;

public class Lexer {
    int pos = 0;
    String input;
    private Token last;

    Lexer(String input) {
        this.last = null;
        this.input = input;
    }

    char curChar() {
        if (pos < input.length()) {
            return input.charAt(pos);
        }
        return 0;
    }

    char nextChar() {
        if (pos + 1 < input.length()) {
            return input.charAt(pos + 1);
        }
        return 0;
    }

    String readNumber() {
        int i = pos;
        while (isDigit(curChar())) {
            pos += 1;
        }
        return input.substring(i, pos);
    }

    Token lexFloat() {
        String left = readNumber();
        if (curChar() == '.' && isDigit(nextChar())) {
            pos += 1;
            String right = readNumber();
            return new Token(TokenType.Number, Double.parseDouble(left + "." + right));
        }
        return new Token(TokenType.Number, Double.parseDouble(left));
    }

    Token Next() {
        last = xNext();
        return last;
    }
    Token Last() {
        return last;
    }

    private Token xNext() {
        while (isWhitespace(curChar())) {
            pos += 1;
        }
        char ch = curChar();

        if (isDigit(ch)) {
            return lexFloat();
        }

        if (isSmallAlpha(ch)) {
            if (pos + 3 > input.length())
                return new Token(TokenType.Error, 0.0);
            String tmp = input.substring(pos, pos + 3);
            pos += 3;
            switch (tmp) {
                case "sin":
                    return new Token(TokenType.Sin, 0.0);
                case "cos":
                    return new Token(TokenType.Cos, 0.0);
                case "tan":
                    return new Token(TokenType.Tan, 0.0);
                case "deg":
                    return new Token(TokenType.Deg, 0.0);
                case "log":
                    return new Token(TokenType.Log, 0.0);
                case "abs":
                    return new Token(TokenType.Abs, 0.0);
            }
            return new Token(TokenType.Error, 0.0);
        }

        pos += 1;
        switch (ch) {
            case 0:
                return new Token(TokenType.Eof, 0.0);
            case '+':
                return new Token(TokenType.Plus, 0.0);
            case '-':
                return new Token(TokenType.Minus, 0.0);
            case '~':
                return new Token(TokenType.Tilda, 0.0);
            case '%':
                return new Token(TokenType.Mod, 0.0);
            case '/':
                return new Token(TokenType.Div, 0.0);
            case '*':
                if (curChar() == '*') {
                    pos += 1;
                    return new Token(TokenType.Exp, 0.0);
                }
                return new Token(TokenType.Mult, 0.0);
            case '!':
                return new Token(TokenType.Fact, 0.0);
            case '&':
                return new Token(TokenType.And, 0.0);
            case '|':
                return new Token(TokenType.Or, 0.0);
            case '^':
                return new Token(TokenType.Xor, 0.0);
            case '<':
                if (curChar() == '<') {
                    pos += 1;
                    return new Token(TokenType.LShift, 0.0);
                }
                return new Token(TokenType.Error, 0.0);
            case '>':
                if (curChar() == '>') {
                    pos += 1;
                    return new Token(TokenType.RShift, 0.0);
                }
                return new Token(TokenType.Error, 0.0);
            case 'E':
                return new Token(TokenType.Number, Math.E);
            case 'P':
                if (curChar() == 'I') {
                    pos += 1;
                    return new Token(TokenType.Number, Math.PI);
                }
                return new Token(TokenType.Error, 0.0);
            case '(':
                return new Token(TokenType.LParen, 0.0);
            case ')':
                return new Token(TokenType.RParen, 0.0);
        }
        return new Token(TokenType.Error, 0.0);
    }

    Token Peek() {
        int ind = pos;
        Token out = Next();
        pos = ind;
        return out;
    }

    Boolean isWhitespace(char ch) {
        if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n') {
            return true;
        }
        return false;
    }

    Boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    Boolean isSmallAlpha(char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    Boolean isBigAlpha(char ch) {
        return ch >= 'A' && ch <= 'Z';
    }
}
