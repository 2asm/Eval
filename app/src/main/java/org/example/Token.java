package org.example;

public class Token {

    TokenType type;
    double value = 0.0;

    public Token(TokenType type, double value) {
        this.type = type;
        this.value = value;
    }
}
