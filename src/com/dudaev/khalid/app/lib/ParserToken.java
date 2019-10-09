package com.dudaev.khalid.app.lib;

import com.dudaev.khalid.sgrammar.Parser;
import com.dudaev.khalid.sgrammar.lib.syntax.Token;

import java.util.regex.Matcher;

// import com.dudaev.khalid.sgrammar.lib.Lexer;

/**
 * ParserToken
 */
public class ParserToken {

    Parser parser;

    public ParserToken(Parser parent) {
        this.parser = parent;
    }

    public Token space (Token token, Matcher m) {
        // System.out.println("############ " + this.lexer.column);
        // token.setLexemLenght        = 0;
//        token.lenght                = 0;
        token.isParse               = false;
        return token;
    }

    public Token nline (Token token, Matcher m) {
        // System.out.println("############ " + this.lexer.column);

        // token.setLexemLenght        = 0;
//        token.lenght                = 0;
        token.isParse               = false;

        // token.column = 1;

        // this.lexer.line ++;
        // this.lexer.column = 1;

        return token;
    }

    // public Token lparen(Token token, Matcher m) {
        
    //     return token;
    // }

    public Token error(Token token, Matcher m){
        // System.err.println("ERROR" + );
        System.err.println("ERROR! TOKEN >> L:" + token.line + " C:" + token.column + " >> '" + token.content + "'");
        return token;
    }

    // public Token actionAS(Token token, Matcher m){
    //     System.out.println(">>> ADD/SUB");
    //     return token;
    // }

    // public void endParsing(){
        
    // }

}