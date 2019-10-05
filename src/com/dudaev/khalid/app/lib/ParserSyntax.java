package com.dudaev.khalid.app.lib;

import java.util.HashMap;
import java.util.Stack;

import com.dudaev.khalid.sgrammar.Parser;
import com.dudaev.khalid.sgrammar.lib.syntax.Token;

/**
 * AParser
 */
public class ParserSyntax {

    Parser parser;

    private Stack<Operator> stackOP         = new Stack<>();
    private Stack<String> stackVAL          = new Stack<>();
    // HashMap<int, String> op;

    public ParserSyntax(Parser parent) {
        this.parser = parent;
    }

    public void opAS(Token token){
        // System.out.println("OP " + token.content + " | " + this.parser.stackNode.size());
        addOP(token, this.parser.stackNode.size());
    }

    public void opMD(Token token){
        // System.out.println("OP " + token.content + " | " + this.parser.stackNode.size());
        addOP(token, this.parser.stackNode.size());
    }

    public void paren(Token token){
        System.out.println("PRN " + token.content + " | " + this.parser.stackNode.size());
    }

    public void lparen(Token token){
        // System.out.println("LPRN " + token.content + " | " + this.parser.stackNode.size());
    }
    
    public void rparen(Token token){
        // System.out.println("RPRN " + token.content + " | " + this.parser.stackNode.size());
    }

    public void fact(Token token){
        // System.out.println("FCT " + token.content + " ");

        switch (token.type) {
            case "lparen": lparen(token); break;
            case "rparen": rparen(token); break;
            default:
                // System.out.println("FCT " + token.content + " ");
                addVAL(token);
                break;
        }
    }

    public void opADD(Token token){
        System.out.println("ADD " + token.content + " ");
    }

    public void opSUB(Token token){
        System.out.println("SUB " + token.content + " ");
    }

    public void opMUL(Token token){
        System.out.println("MUL " + token.content + " ");
    }

    public void opDIV(Token token){
        System.out.println("DIV " + token.content + " ");
    }

    public void ident(Token token){
        System.out.println("IDT " + token.content + " ");
    }
    public void number(Token token){
        System.out.println("NUM " + token.content + " ");
    }

    private void addVAL(Token val){
        stackVAL.push(val.content);

    }

    private void addOP(Token operator, Integer level){
        // String a = null;
        // String b = null;
        while(stackOP.size() > 0 && level <= stackOP.peek().level){
            // stackVAL.push(stackOP.pop().op.content);
            stackVAL.push(exec(stackVAL.pop(), stackVAL.pop(), stackOP.pop().op.content));
        }
        stackOP.push(new Operator(operator, level));
    }

    private String exec(String b, String a, String op) {
        Double res = 0.0;

        switch (op) {
            case "+": res = Double.parseDouble(a) + Double.parseDouble(b); break;
            case "-": res = Double.parseDouble(a) - Double.parseDouble(b); break;
            case "*": res = Double.parseDouble(a) * Double.parseDouble(b); break;
            case "/": res = Double.parseDouble(a) / Double.parseDouble(b); break;
            default:
                break;
        }

        return res.toString();
    }

    public void error(Token token){
        System.err.println("ERROR! SYNTAX >> L:" + token.line + " C:" + token.column + " >> '" + token.content + "'");
    } 

    public String endParsing(){
        // stackVAL.add(stackOP.pop().op.content);
        while(!stackOP.empty()) {
            // stackVAL.push(stackOP.pop().op.content);
            stackVAL.push(exec(stackVAL.pop(), stackVAL.pop(), stackOP.pop().op.content));
        }

        // System.out.println(stackVAL.toString());
        return stackVAL.pop().toString();
    }
    
    private class Operator {
        public Token op = new Token();
        public Integer level = 0;
        
        Operator(Token op, Integer level){
            this.op = op;
            this.level = level;
        }
    }
}


