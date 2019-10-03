package com.dudaev.khalid.app.lib;

import com.dudaev.khalid.tools.File;
import com.dudaev.khalid.sgrammar.Parser;

/**
 * Parser
 */
public class Expression {

    private static File file        = new File();
    private static Parser parser    = new Parser();

	public Expression() {    
        parser.setTokenAction(new ParserToken(parser));
        parser.setSyntaxAction(new ParserSyntax(parser));

        String rulesText                = file.read("expression.rule");

        parser.rules(rulesText);
    }

    public void compile(String source){
        parser.source(source);
        parser.start();
    }

    


    
}