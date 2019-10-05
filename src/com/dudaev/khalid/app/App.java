package com.dudaev.khalid.app;

import com.dudaev.khalid.app.lib.Expression;

public class App {

    private static Expression expression    = new Expression();
    private static String expr = "41 + 4 * 9 / (3 + 6) + 55";

    public static void main(String[] args) {
        String res = expression.eval(expr);
        System.out.println(expr + " = " + res);
    }
    
}