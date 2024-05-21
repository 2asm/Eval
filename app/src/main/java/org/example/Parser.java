package org.example;

import java.util.ArrayList;

public class Parser {
    private ArrayList<Token> tokens;
    private int pos;

    Parser(String input) {
        Lexer lexer = new Lexer(input);
        this.tokens = lexer.Lex();
        this.pos = 0;
    }

    private Token Peek() {
        return tokens.get(pos);
    }

    private Token Next() {
        Token out = tokens.get(pos);
        pos += 1;
        return out;
    }

    private int[] prefixBindingPower(Token t) {
        int[] out = { -1, -1 };
        switch (t.type) {
            case TokenType.Plus:
            case TokenType.Minus:
            case TokenType.Tilda:
                out[1] = 95;
                return out;
            default:
                return out;
        }
    }

    private int[] postfixBindingPower(Token t) {
        int[] out = { -1, -1 };
        switch (t.type) {
            case TokenType.LParen:
                out[0] = 99;
                return out;
            case TokenType.Fact:
                out[0] = 97;
                return out;
            default:
                return out;
        }
    }

    private int[] infixBindingPower(Token t) {
        int[] out = { -1, -1 };
        switch (t.type) {
            case TokenType.Exp:
                out[0] = 93;
                out[1] = 92;
                return out;
            case TokenType.Mult:
            case TokenType.Mod:
            case TokenType.Div:
                out[0] = 90;
                out[1] = 91;
                return out;
            case TokenType.Plus:
            case TokenType.Minus:
                out[0] = 88;
                out[1] = 89;
                return out;
            case TokenType.LShift:
            case TokenType.RShift:
                out[0] = 86;
                out[1] = 87;
                return out;
            case TokenType.And:
                out[0] = 84;
                out[1] = 85;
                return out;
            case TokenType.Xor:
                out[0] = 82;
                out[1] = 83;
                return out;
            case TokenType.Or:
                out[0] = 80;
                out[1] = 81;
                return out;
            default:
                return out;
        }
    }

    private Token prefixOp(Token op, Token rhs) {
        if (rhs.type != TokenType.Number)
            return new Token(TokenType.Error, 0);
        switch (op.type) {
            case TokenType.Plus:
                return rhs;
            case TokenType.Minus:
                rhs.value = -rhs.value;
                return rhs;
            case TokenType.Tilda:
                if (rhs.value % 1 != 0)
                    return new Token(TokenType.Error, 0);
                rhs.value = ~(long) rhs.value;
                return rhs;
            default:
                return new Token(TokenType.Error, 0);
        }
    }

    private long Factorial(long n) {
        long ans = 1;
        for (long i = 1; i <= n; i++) {
            ans *= i;
        }
        return ans;
    }

    private Token posfixOp(Token op, Token lhs) {
        switch (op.type) {
            case TokenType.Fact:
                if (lhs.type != TokenType.Number)
                    return new Token(TokenType.Error, 0);
                if (lhs.value % 1 != 0)
                    return new Token(TokenType.Error, 0);
                lhs.value = Factorial((long) lhs.value);
                return lhs;
            case TokenType.LParen:
                Token rhs = expr_bp(0);
                if (rhs.type != TokenType.Number)
                    return new Token(TokenType.Error, 0);
                Token t = Next();
                if (t.type != TokenType.RParen)
                    return new Token(TokenType.Error, 0);
                switch (lhs.type) {
                    case TokenType.Sin:
                        return new Token(TokenType.Number, Math.sin(rhs.value));
                    case TokenType.Cos:
                        return new Token(TokenType.Number, Math.cos(rhs.value));
                    case TokenType.Tan:
                        return new Token(TokenType.Number, Math.tan(rhs.value));
                    case TokenType.Abs:
                        return new Token(TokenType.Number, Math.abs(rhs.value));
                    case TokenType.Log:
                        return new Token(TokenType.Number, Math.log(rhs.value));
                    case TokenType.Deg:
                        return new Token(TokenType.Number, Math.toRadians(rhs.value));
                    default:
                        return new Token(TokenType.Error, 0);
                }
            default:
                return new Token(TokenType.Error, 0);
        }
    }

    private Token infixOp(Token op, Token lhs, Token rhs) {
        if (lhs.type != TokenType.Number || rhs.type != TokenType.Number)
            return new Token(TokenType.Error, 0);

        double out;
        switch (op.type) {
            case TokenType.Plus:
                out = lhs.value + rhs.value;
                break;
            case TokenType.Minus:
                out = lhs.value - rhs.value;
                break;
            case TokenType.Mult:
                out = lhs.value * rhs.value;
                break;
            case TokenType.Mod:
                out = lhs.value % rhs.value;
                break;
            case TokenType.Div:
                out = lhs.value / rhs.value;
                break;
            case TokenType.LShift:
                if (lhs.value % 1 != 0 || rhs.value % 1 != 0)
                    return new Token(TokenType.Error, 0);
                out = (long) lhs.value << (long) rhs.value;
                break;
            case TokenType.RShift:
                if (lhs.value % 1 != 0 || rhs.value % 1 != 0)
                    return new Token(TokenType.Error, 0);
                out = (long) lhs.value >> (long) rhs.value;
                break;
            case TokenType.Xor:
                if (lhs.value % 1 != 0 || rhs.value % 1 != 0)
                    return new Token(TokenType.Error, 0);
                out = (long) lhs.value ^ (long) rhs.value;
                break;
            case TokenType.Or:
                if (lhs.value % 1 != 0 || rhs.value % 1 != 0)
                    return new Token(TokenType.Error, 0);
                out = (long) lhs.value | (long) rhs.value;
                break;
            case TokenType.And:
                if (lhs.value % 1 != 0 || rhs.value % 1 != 0)
                    return new Token(TokenType.Error, 0);
                out = (long) lhs.value & (long) rhs.value;
                break;
            case TokenType.Exp:
                out = Math.pow(lhs.value, rhs.value);
                break;
            default:
                return new Token(TokenType.Error, 0);
        }
        return new Token(TokenType.Number, out);
    }

    double expr() throws Error {
        Token out = expr_bp(0);
        if (Peek().type == TokenType.Eof && out.type == TokenType.Number)
            return out.value;
        throw new Error("Can't compile the input");
    }

    private Token expr_bp(int min_bp) {
        Token lhs = Next();
        switch (lhs.type) {
            case TokenType.Number:
                break;
            case TokenType.LParen:
                lhs = expr_bp(0);
                Token t = Next();
                if (t.type != TokenType.RParen)
                    return new Token(TokenType.Error, 0);
                break;
            default:
                int[] bp = prefixBindingPower(lhs);
                if (bp[1] == -1)
                    break;
                Token rhs = expr_bp(bp[1]);
                lhs = prefixOp(lhs, rhs);
        }
        while (true) {
            Token op = Peek();
            if (op.type == TokenType.Eof)
                break;
            int[] bp = postfixBindingPower(op);
            if (bp[0] != -1) {
                if (bp[0] < min_bp)
                    break;
                Next();
                lhs = posfixOp(op, lhs);
                continue;
            }
            bp = infixBindingPower(op);
            if (bp[0] != -1 && bp[1] != -1) {
                if (bp[0] < min_bp)
                    break;
                Next();
                Token rhs = expr_bp(bp[1]);
                lhs = infixOp(op, lhs, rhs);
                continue;
            }
            break;
        }
        return lhs;
    }
}
