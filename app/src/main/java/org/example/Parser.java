package org.example;

public class Parser {
    Lexer lexer;

    Parser(String input) {
        lexer = new Lexer(input);
    }

    int[] prefixBindingPower(Token t) {
        int[] out = { -1, -1 };
        switch (t.type) {
            case Token.TokenType.Plus:
            case Token.TokenType.Minus:
            case Token.TokenType.Tilda:
                out[1] = 95;
                return out;
            default:
                return out;
        }
    }

    int[] postfixBindingPower(Token t) {
        int[] out = { -1, -1 };
        switch (t.type) {
            case Token.TokenType.LParen:
                out[0] = 99;
                return out;
            case Token.TokenType.Fact:
                out[0] = 97;
                return out;
            default:
                return out;
        }
    }

    int[] infixBindingPower(Token t) {
        int[] out = { -1, -1 };
        switch (t.type) {
            case Token.TokenType.Exp:
                out[0] = 93;
                out[1] = 92;
                return out;
            case Token.TokenType.Mult:
            case Token.TokenType.Mod:
            case Token.TokenType.Div:
                out[0] = 90;
                out[1] = 91;
                return out;
            case Token.TokenType.Plus:
            case Token.TokenType.Minus:
                out[0] = 88;
                out[1] = 89;
                return out;
            case Token.TokenType.LShift:
            case Token.TokenType.RShift:
                out[0] = 86;
                out[1] = 87;
                return out;
            case Token.TokenType.And:
                out[0] = 84;
                out[1] = 85;
                return out;
            case Token.TokenType.Xor:
                out[0] = 82;
                out[1] = 83;
                return out;
            case Token.TokenType.Or:
                out[0] = 80;
                out[1] = 81;
                return out;
            default:
                return out;
        }
    }

    Token prefixOp(Token op, Token rhs) {
        if (rhs.type != Token.TokenType.Number)
            return new Token(Token.TokenType.Error, 0.0);
        switch (op.type) {
            case Token.TokenType.Plus:
                return rhs;
            case Token.TokenType.Minus:
                rhs.value = -rhs.value;
                return rhs;
            case Token.TokenType.Tilda:
                Long num = rhs.value.longValue();
                if (rhs.value - num != 0)
                    return new Token(Token.TokenType.Error, 0.0);
                rhs.value = Double.parseDouble((~num)+"");
                return rhs;
            default:
                return new Token(Token.TokenType.Error, 0.0);
        }
    }

    Long Factorial(Long n) {
        try {
            Long ans = 1L;
            for (Long i = 1L; i <= n; i++) {
                ans *= i;
            }
            return ans;
        } catch (Exception e) {
            return null;
        }
    }

    Token posfixOp(Token op, Token lhs) {
        switch (op.type) {
            case Token.TokenType.Fact:
                if (lhs.type != Token.TokenType.Number)
                    return new Token(Token.TokenType.Error, 0.0);
                Long fnum = lhs.value.longValue();
                if (lhs.value - fnum != 0)
                    return new Token(Token.TokenType.Error, 0.0);
                Long f = Factorial(fnum);
                if (f == null)
                    return new Token(Token.TokenType.Error, 0.0);
                lhs.value = Double.parseDouble(f+"");
                return lhs;
            case Token.TokenType.LParen:
                Token rhs = expr_bp(0);
                if (rhs.type != Token.TokenType.Number)
                    return new Token(Token.TokenType.Error, 0.0);
                Token t = lexer.Next();
                if (t.type != Token.TokenType.RParen)
                    return new Token(Token.TokenType.Error, 0.0);
                switch (lhs.type) {
                    case Token.TokenType.Sin:
                        return new Token(Token.TokenType.Number, Math.sin(rhs.value));
                    case Token.TokenType.Cos:
                        return new Token(Token.TokenType.Number, Math.cos(rhs.value));
                    case Token.TokenType.Tan:
                        return new Token(Token.TokenType.Number, Math.tan(rhs.value));
                    case Token.TokenType.Abs:
                        return new Token(Token.TokenType.Number, Math.abs(rhs.value));
                    case Token.TokenType.Log:
                        return new Token(Token.TokenType.Number, Math.log(rhs.value));
                    case Token.TokenType.Deg:
                        return new Token(Token.TokenType.Number, Math.toRadians(rhs.value));
                    default:
                        return new Token(Token.TokenType.Error, 0.0);
                }
            default:
                return new Token(Token.TokenType.Error, 0.0);
        }
    }

    Token infixOp(Token op, Token lhs, Token rhs) {
        if (rhs.type != Token.TokenType.Number)
            return new Token(Token.TokenType.Error, 0.0);
        if (lhs.type != Token.TokenType.Number)
            return new Token(Token.TokenType.Error, 0.0);

        Double out;
        Long lnum, rnum;
        switch (op.type) {
            case Token.TokenType.Plus:
                out = lhs.value + rhs.value;
                break;
            case Token.TokenType.Minus:
                out = lhs.value - rhs.value;
                break;
            case Token.TokenType.Mult:
                out = lhs.value * rhs.value;
                break;
            case Token.TokenType.Mod:
                out = lhs.value % rhs.value;
                break;
            case Token.TokenType.Div:
                out = lhs.value / rhs.value;
                break;
            case Token.TokenType.LShift:
                lnum = lhs.value.longValue();
                if (lhs.value - lnum != 0)
                    return new Token(Token.TokenType.Error, 0.0);
                rnum = rhs.value.longValue();
                if (rhs.value - rnum != 0)
                    return new Token(Token.TokenType.Error, 0.0);
                out = Double.parseDouble((lnum << rnum)+"");
                break;
            case Token.TokenType.RShift:
                lnum = lhs.value.longValue();
                if (lhs.value - lnum != 0)
                    return new Token(Token.TokenType.Error, 0.0);
                rnum = rhs.value.longValue();
                if (rhs.value - rnum != 0)
                    return new Token(Token.TokenType.Error, 0.0);
                out = Double.parseDouble((lnum >> rnum)+"");
                break;
            case Token.TokenType.Xor:
                lnum = lhs.value.longValue();
                if (lhs.value - lnum != 0)
                    return new Token(Token.TokenType.Error, 0.0);
                rnum = rhs.value.longValue();
                if (rhs.value - rnum != 0)
                    return new Token(Token.TokenType.Error, 0.0);
                out = Double.parseDouble((lnum ^ rnum)+"");
                break;
            case Token.TokenType.Or:
                lnum = lhs.value.longValue();
                if (lhs.value - lnum != 0)
                    return new Token(Token.TokenType.Error, 0.0);
                rnum = rhs.value.longValue();
                if (rhs.value - rnum != 0)
                    return new Token(Token.TokenType.Error, 0.0);
                out = Double.parseDouble((lnum | rnum)+"");
                break;
            case Token.TokenType.And:
                lnum = lhs.value.longValue();
                if (lhs.value - lnum != 0)
                    return new Token(Token.TokenType.Error, 0.0);
                rnum = rhs.value.longValue();
                if (rhs.value - rnum != 0)
                    return new Token(Token.TokenType.Error, 0.0);
                out = Double.parseDouble((lnum & rnum)+"");
                break;
            case Token.TokenType.Exp:
                out = Math.pow(lhs.value, rhs.value);
                break;
            default:
                return new Token(Token.TokenType.Error, 0.0);
        }
        return new Token(Token.TokenType.Number, out);
    }

    Double expr() {
        Token out = expr_bp(0);

        Token l = lexer.Last();
        if (l.type != Token.TokenType.Eof) {
            return null;
        }

        if (out.type != Token.TokenType.Number) {
            return null;
        }
        return out.value;
    }

    Token expr_bp(int min_bp) {
        Token lhs = lexer.Next();
        switch (lhs.type) {
            case Token.TokenType.Number:
                break;
            case Token.TokenType.LParen:
                lhs = expr_bp(0);
                Token t = lexer.Next();
                if (t.type != Token.TokenType.RParen)
                    return new Token(Token.TokenType.Error, 0.0);
                break;
            default:
                int[] bp = prefixBindingPower(lhs);
                if (bp[1] == -1)
                    break;
                Token rhs = expr_bp(bp[1]);
                lhs = prefixOp(lhs, rhs);
        }
        while (true) {
            Token op = lexer.Peek();
            if (op.type == Token.TokenType.Eof)
                break;
            int[] bp = postfixBindingPower(op);
            if (bp[0] != -1) {
                if (bp[0] < min_bp)
                    break;
                lexer.Next();
                lhs = posfixOp(op, lhs);
                continue;
            }
            bp = infixBindingPower(op);
            if (bp[0] != -1 && bp[1] !=-1) {
                if (bp[0] < min_bp)
                    break;
                lexer.Next();
                Token rhs = expr_bp(bp[1]);
                lhs = infixOp(op, lhs, rhs);
                continue;
            }
            break;
            // return new Token(Token.TokenType.Error, 0.0);
        }
        return lhs;
    }

}
