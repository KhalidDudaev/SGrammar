package com.dudaev.khalid.app.lib;

import com.dudaev.khalid.sgrammar.Parser;
import com.dudaev.khalid.sgrammar.lib.syntax.Token;

import java.util.Stack;

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
        // System.out.println("&&& opAS " + token.content);
        // pushOP(token, this.parser.stackNode.size());
    }

    public void opMD(Token token){
        // System.out.println("OP " + token.content + " | " + this.parser.stackNode.size());
        // pushOP(token, this.parser.stackNode.size());
    }

    public void paren(Token token){
        // System.out.println("PRN " + token.content + " | " + this.parser.stackNode.size());
    }

    public void lparen(Token token){
        // System.out.println("LPRN " + token.content + " | " + this.parser.stackNode.size());
    }
    
    public void rparen(Token token){
        // System.out.println("RPRN " + token.content + " | " + this.parser.stackNode.size());
    }

    public void fact(Token token){
        // System.out.println("FCT " + token.content + " ");

        switch (token.getType()) {
            case "lparen": lparen(token); break;
            case "rparen": rparen(token); break;
            default:
                // System.out.println("FCT " + token.content + " ");
                pushVAL(token.getContent());
                break;
        }
    }

    public void opADD(Token token){
        // System.out.println("ADD " + token.content + " ");
        pushOP(token, this.parser.stackNode.size());
    }

    public void opSUB(Token token){
        // System.out.println("SUB " + token.content + " ");
        pushOP(token, this.parser.stackNode.size());
    }

    public void opMUL(Token token){
        // System.out.println("MUL " + token.content + " ");
        pushOP(token, this.parser.stackNode.size());
    }

    public void opDIV(Token token){
        // System.out.println("DIV " + token.content + " ");
        pushOP(token, this.parser.stackNode.size());
    }

    public void opPOW(Token token){
        // System.out.println("POW " + token.content + " ");
        pushOP(token, this.parser.stackNode.size());
    }

    public void ident(Token token){
        // System.out.println("IDT " + token.content + " ");
    }
    public void number(Token token){
        // System.out.println("NUM " + token.content + " ");
    }

    private void pushVAL(String val){
        // System.out.println(val);
        stackVAL.push(val);

    }

    private void pushOP(Token operator, Integer level){
        while(stackOP.size() > 0 && level <= stackOP.peek().level){
            stackVAL.push(exec(stackVAL.pop(), stackVAL.pop(), stackOP.pop().op.getContent()));
        }
        stackOP.push(new Operator(operator, level));
    }

    private String exec(String b, String a, String op) {
        String res = null;
        switch (op) {
            case "+": res = execADD(a, b); break;
            case "-": res = execSUB(a, b); break;
            case "*": res = execMUL(a, b); break;
            case "/": res = execDIV(a, b); break;
            case "^": res = execPOW(a, b); break;
            default: break;
        }
        return res;
    }

    private String execADD(String a, String b){
        //System.out.println("ADD ");
        Double res = Double.parseDouble(a) + Double.parseDouble(b);
        return res.toString();
    }

    private String execSUB(String a, String b){
        //System.out.println("SUB ");
        Double res = Double.parseDouble(a) - Double.parseDouble(b);
        return res.toString();
    }

    private String execMUL(String a, String b){
        //System.out.println("MUL ");
        Double res = Double.parseDouble(a) * Double.parseDouble(b);
        return res.toString();
    }

    private String execDIV(String a, String b){
        //System.out.println("DIV ");
        Double res = Double.parseDouble(a) / Double.parseDouble(b);
        return res.toString();
    }

    private String execPOW(String a, String b){
        //System.out.println("POW ");
        Double res = Math.pow(Double.parseDouble(a), Double.parseDouble(b));
        return res.toString();
    }

    public void error(Token token){
        System.err.println("ERROR! SYNTAX >> L:" + token.getLine() + " C:" + token.getColumn() + " >> '" + token.getContent() + "'");
    } 

    public String endParsing(){
        // stackVAL.add(stackOP.pop().op.content);
        while(!stackOP.empty()) {
            // stackVAL.push(stackOP.pop().op.content);
            stackVAL.push(exec(stackVAL.pop(), stackVAL.pop(), stackOP.pop().op.getContent()));
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


