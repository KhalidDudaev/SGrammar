package com.dudaev.khalid.app;

import com.dudaev.khalid.app.lib.Expression;

public class App {

    private static Expression expression    = new Expression();
    private static String expr              = "41 + (15 - 11) * 3^2 / (3 + 2 * ( 4 - 1 )) + 55";

    public static void main(String[] args) {
        String res = expression.eval(expr);
        System.out.println(expr + " = " + res);
    }
    
}
