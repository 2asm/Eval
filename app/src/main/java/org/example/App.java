package org.example;

public class App {
    public static void main(String[] args) {
        // String s = "~77 + 34.34*5/(4+5-3)**1.2 + (4^1>>3<<4&1|4^45) + sin(deg(30)) +
        // sin(PI/2) + E+abs(-45) + log(12!) ";
        String s = "23+5/3-4 - PI + E";
        try {
            Parser p = new Parser(s);
            System.out.println(s + " --> " + p.expr());
        } catch (Error e) {
            System.out.println(e);
        }
    }
}
