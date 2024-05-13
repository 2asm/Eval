package org.example;

public class App {

    public static void main(String[] args) {
        String s = "23+5/3-4";
        Parser p = new Parser(s);
        System.out.println(s + " --> " + p.expr());
    }
}
