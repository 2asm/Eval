package org.example;

public class Token {

    TokenType type;
    Double value = 0.0;

    public Token(TokenType type, Double value) {
        this.type = type;
        this.value = value;
    }
}
