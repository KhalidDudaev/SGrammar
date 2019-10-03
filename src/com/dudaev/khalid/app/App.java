package com.dudaev.khalid.app;

import com.dudaev.khalid.app.lib.Expression;

public class App {

    private static Expression expression    = new Expression();

    public static void main(String[] args) {

        expression.compile("41 + 4 * 9 / (3 + 6) + 55");

    }
    
}