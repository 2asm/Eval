package org.example;

public enum TokenType {
    Error,
    Eof,

    LParen,
    RParen,

    Exp,

    Tilda, // bitwise not
    Fact,

    Mult,
    Div,
    Mod,

    Plus,
    Minus,

    LShift,
    RShift,

    And, // bitwise And
    Xor,
    Or,

    Number, // long double

    // constants
    PI, // 3.141592653589793
    E, // 2.718281828459045

    // builtin functions
    Sin,
    Cos,
    Tan,
    Deg, // degree to radian
    Log, // natural log
    Abs;
}
