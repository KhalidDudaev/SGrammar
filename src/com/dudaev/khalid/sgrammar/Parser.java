package com.dudaev.khalid.sgrammar;

import com.dudaev.khalid.sgrammar.lib.Grammar;
import com.dudaev.khalid.sgrammar.lib.Lexer;
import com.dudaev.khalid.sgrammar.lib.grammar.*;
import com.dudaev.khalid.sgrammar.lib.syntax.Token;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Parser
 */
public class Parser {

    // private String source;

    private int pos                     = 0;
    public Stack<GProduction> stackProd   = new Stack<>();
    public Stack<GRule> stackRule   = new Stack<>();
    public Stack<GNode> stackNode   = new Stack<>();

    // private Table table;
    private GTokens gtokens             = new GTokens();

    private Lexer lexer                 = new Lexer();
    private Grammar grammar             = new Grammar();
    
    private ArrayList<Token> tokens     = new ArrayList<>();
    
    private boolean skeepNoParse        = true;
    
    
    private String rulesText;
    private String sourceText;
    private Object actionLexer;
    private Object actionSyntax;

    public Parser() {

    }

    private void init(){

        this.grammar.make(this.rulesText);

        this.lexer.setAction(actionLexer);
        this.lexer.setTokenPatterns(this.grammar.getGTokens());
        this.lexer.scan(this.sourceText);

        this.gtokens    = this.lexer.gtoken();
        this.tokens.addAll(this.lexer.getTokens());
        Token endToken = new Token();
        endToken.type = "$";
        this.tokens.add(endToken);
    }

    public void source(String source) {
        this.sourceText = source;
    }

    public void rules(String rules) {
        this.rulesText = rules;
    }

    public void setTokenAction(Object ac){
        actionLexer = ac;
    }

    public void setSyntaxAction(Object ac){
        actionSyntax = ac;
    }

    private void push2Stack(GProduction prod) {
        for ( int i = prod.getNodes().size(); i > 0; i--) {
            this.stackNode.push(prod.getNodes().get(i-1));
        }
    }

    public GNode peekStack(){
        return this.stackNode.peek();
    }

    public GNode nextStack(){
        return this.stackNode.pop();
    }

    public Token peek(int relative){
        if((this.pos + relative) < this.tokens.size()) return this.tokens.get(this.pos + relative);
        System.err.println("No has next token");
        throw new ArrayIndexOutOfBoundsException();
        // return new Token();
    }

    public Token nextToken(){
        Token token = peek(0);
        this.pos++;
        if(skeepNoParse) skeepNoParse();
        return token;
    }

    public boolean hasNextToken(){
        Token token = peek(0);
        // if(this.pos < this.tokens.size()) return true;
        if(this.pos < this.tokens.size() && token.content != "$") return true;
        else return false;
    }

    private boolean skeepNoParse(){
        while(hasNextToken() && !peek(0).isParse){
            nextToken();
        }
        return hasNextToken();
    }

    private TableSYNTAX syntax(){
        return grammar.getTable().getSYNTAX();
    }

    public Object start(String source){
        this.sourceText = source;
        return start();
    }

    public Object start(){
        this.init();

        stackNode.push(new GNode("$", null, null, true));
        stackNode.push(grammar.getRules().list().get(0).getNode());

        return parse();
    }

    private Object parse(){
        boolean error = false;
        while(!error && peekStack().getNode() != "$"){

            GNode sp            = peekStack();
            Token tp            = peek(0);

            String node         = sp.getNode();
            GRule rule          = grammar.getRules().get(sp.getRule());
            GProduction prod    = syntax().getProduction(tp.type, sp.getNode());

            String acRule       = rule.getAction();
            String acProd       = sp.getAction();
            // String acProd       = null;
            
            // if(prod != null) acProd = prod.getAction();

            // if(acRule != null) runAction(acRule, tp);

            if(sp.isTERMINAL()){
                if(node.equals("e")){
                    nextStack();
                } 
                else if(node.matches(tp.type)) {
                    runAction(acRule, tp);
                    runAction(acProd, tp);
                    nextStack();
                    nextToken();
                } 
            } else {
                // GNode rem = nextStack();
                // GProduction prod = syntax().getProduction(tp.type, rem.getNode());
                nextStack();

                // runAction(acProd, tp);

                if(prod != null) {
                    push2Stack(prod);
                } else {
                    error = true;
                    runAction("error", tp);
                }
            }
        }
        return endAction();
    }

    private void runAction(String actionName, Token token) {
        Method meth             = null;
        boolean methodExists    = false;

        try {
            meth = actionSyntax.getClass().getDeclaredMethod( actionName, Token.class );
            try {
                methodExists                                    = true;
                // retToken = (Token) 
                meth.invoke(actionSyntax, token);
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NullPointerException e) {
                methodExists                                    = false;
            }
        } catch (NullPointerException | SecurityException | NoSuchMethodException e) { 
            // System.err.println("WARNING: Not found action of parser - " + actionName);
        }

        // refToken

        // return retToken;
    }

    private Object endAction() {
        Method meth             = null;
        boolean methodExists    = false;
        Object result = null;

        try {
            meth = actionSyntax.getClass().getDeclaredMethod( "endParsing" );
            try {
                methodExists                                    = true;
                // retToken = (Token) 
                result = meth.invoke(actionSyntax);
                
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NullPointerException e) {
                methodExists                                    = false;
            }
        } catch (NullPointerException | SecurityException | NoSuchMethodException e) { 
            System.err.println("WARNING: Not found action of parser - result");
        }

        // refToken

        return result;
    }
    


}