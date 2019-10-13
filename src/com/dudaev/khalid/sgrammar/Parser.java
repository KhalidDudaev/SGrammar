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

    private int pos                         = 0;
    public Stack<GProduction> stackProd     = new Stack<>();
    public Stack<GRule> stackRule           = new Stack<>();
    public Stack<GNode> stackNode           = new Stack<>();
    private Lexer lexer                     = new Lexer();
    private Grammar grammar                 = new Grammar();
    private ArrayList<Token> tokens         = new ArrayList<>();
    private boolean skipNoParse            = true;
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
        this.tokens.addAll(this.lexer.getTokens());
        Token endToken = new Token();
        endToken.setType("$");
        this.tokens.add(endToken);
    }

    /**
     * set the source as a string to parse
     * @param source
     */
    public void source(String source) {
        this.sourceText = source;
    }

    /**
     * set rules for the parser
     * @param rules
     */
    public void rules(String rules) {
        this.rulesText = rules;
    }

    /**
     * set an instance of a class with action methods for lexical parser rules 
     * @param ac
     */
    public void setTokenAction(Object ac){
        actionLexer = ac;
    }

    /**
     * set an instance of a class with action methods for syntax parser rules 
     * @param ac
     */
    public void setSyntaxAction(Object ac){
        actionSyntax = ac;
    }

    private void push2Stack(GProduction prod) {
        for ( int i = prod.getNodes().size(); i > 0; i--) {
            this.stackNode.push(prod.getNodes().get(i-1));
        }
    }

    /**
     * view the value that is on top of the stack
     * @return
     */
    public GNode peekStack(){
        return this.stackNode.peek();
    }

    /**
     * get value that is on top of the stack and to shift the stack
     * @return
     */
    public GNode nextStack(){
        return this.stackNode.pop();
    }

    /**
     * get a token from the current position of the source.
     * @param relative is a shift relative to current position - positive or negative 
     * @return
     */
    public Token peek(int relative){
        if((this.pos + relative) < this.tokens.size()) return this.tokens.get(this.pos + relative);
        System.err.println("No has next token");
        throw new ArrayIndexOutOfBoundsException();
    }

    /**
     * get a token from the next position of the source and increase position counter
     * @return
     */
    public Token nextToken(){
        this.pos++;
        Token token = peek(0);
        if(skipNoParse) skipNoParse();
        return token;
    }

    /**
     * check if the next token is available
     * @return
     */
    public boolean hasNextToken(){
        Token token = peek(0);
        if(this.pos < this.tokens.size() && token.getContent() != "$") return true;
        else return false;
    }

    /**
     * start parsing
     * @param source
     */
    public Object start(String source){
        this.sourceText = source;
        return start();
    }

    /**
     * start parsing
     * @return
     */
    public Object start(){
        this.init();

        stackNode.push(new GNode("$", null, null, true));
        stackNode.push(grammar.getRules().list().get(0).getNode());

        return parse();
    }

    ////#region ####################################################################################

    private boolean skipNoParse(){
        while(hasNextToken() && !peek(0).isParse()){
            nextToken();
        }
        return hasNextToken();
    }

    private TableSYNTAX syntax(){
        return grammar.getTable().getSYNTAX();
    }

    private Object parse(){
        boolean error = false;
        while(!error && peekStack().getNode() != "$"){

            GNode sp            = peekStack();
            Token tp            = peek(0);
            String node         = sp.getNode();
            GRule rule          = grammar.getRules().get(sp.getRule());
            GProduction prod    = syntax().getProduction(tp.getType(), sp.getNode());
            String acRule       = rule.getAction();
            String acProd       = sp.getAction();

            if(sp.isTERMINAL()){
                if(node.equals("e")){
                    nextStack();
                } 
                else if(node.matches(tp.getType())) {
                    runAction(acRule, tp);
                    runAction(acProd, tp);
                    nextStack();
                    nextToken();
                } 
            } else {
                nextStack();
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

        try {
            meth = actionSyntax.getClass().getDeclaredMethod( actionName, Token.class );
            try {
                meth.invoke(actionSyntax, token);
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NullPointerException e) {

            }
        } catch (NullPointerException | SecurityException | NoSuchMethodException e) { 
            // System.err.println("WARNING: Not found action of parser - " + actionName);
        }
    }

    private Object endAction() {
        Method meth     = null;
        Object result   = null;

        try {
            meth = actionSyntax.getClass().getDeclaredMethod( "endParsing" );
            try {
                result = meth.invoke(actionSyntax);
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NullPointerException e) {

            }
        } catch (NullPointerException | SecurityException | NoSuchMethodException e) { 
            System.err.println("WARNING: Not found action of parser - result");
        }

        return result;
    }

    ////#endregion #################################################################################
}