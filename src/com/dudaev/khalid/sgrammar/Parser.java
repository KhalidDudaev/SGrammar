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
        // this.lexer        = lexer;
        // this.grammar    = grammar;

        // this.tokens.addAll(lexer.getTokens());
        // this.table      = table;
        // stackRule.addAll(gtokens.listGTokens());
        // this.stackRule.addAll(grammar.getGTokens().listGTokens());


        // this.init();
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

    public void start(){
        this.init();

        stackNode.push(new GNode("$", null, null, true));
        stackNode.push(grammar.getRules().list().get(0).getNode());

        parse();
    }

    private void parse(){
        boolean error = false;
        while(!error && peekStack().getNode() != "$"){

            GNode sp = peekStack();
            Token tp = peek(0);
            // System.out.println("STACK: " + stackNode);
            // System.out.println("TOKEN: " + tp.type);

            String node     = sp.getNode();
            GRule rule      = grammar.getRules().get(sp.getRule());
            String ac       = null;
            
            // if(rule != null && rule.getAction() != null) {
                ac = rule.getAction();
            //     runAction(ac, tp);
            // }
            
            // runAction(sp.getAction(), tp);

            if(sp.isTERMINAL()){

                if(node.equals("e")){
                    // runAction(ac, tp);
                    nextStack();
                } 
                // else
                if(sp.isTERMINAL() && node.matches(tp.type)) {
                    // System.out.println("sp && tp | '" + node + "' : '" + tp.type + "'");
                    // System.out.println("#####: "+ node + " : " + tp.content + " > " + sp.getRule());

                    // runAction(gtokens.get(tp.type).getAction(), tp);

                    // if(rule != null) System.out.println("RUL: " + rule.getName());

                    // runAction(sp.getAction(), tp);
                    runAction(ac, tp);

                    nextStack();
                    nextToken();
                    // skeepNoParse();
                } 
            } else {

                // runAction(ac, tp);

                GNode rem = nextStack();
                GProduction prod = syntax().getProduction(tp.type, rem.getNode());
                if(prod != null) {
                    push2Stack(prod);
                } else {
                    error = true;
                    // System.err.println("ERROR! SYNTAX: " + tp);
                    runAction("error", tp);
                }
            }
        }

        endAction();

        // System.out.println("END MATCHING");
        // System.out.println(endAction());

    }

    private void runAction(String actionName, Token token) {
        Method meth             = null;
        boolean methodExists    = false;
    //     // ArrayList<Objects> ret = new ArrayList<>();
        // Token retToken = null;

        // if(actionName != null) System.out.println("&&&&&&&&&&&&&&&&& " + actionName);

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
            System.err.println("WARNING: Not found action of parser - " + actionName);
        }

        // refToken

        // return retToken;
    }

    private void endAction() {
        Method meth             = null;
        boolean methodExists    = false;
        // String result = "";

        try {
            meth = actionSyntax.getClass().getDeclaredMethod( "endParsing" );
            try {
                methodExists                                    = true;
                // retToken = (Token) 
                meth.invoke(actionSyntax);
                
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NullPointerException e) {
                methodExists                                    = false;
            }
        } catch (NullPointerException | SecurityException | NoSuchMethodException e) { 
            System.err.println("WARNING: Not found action of parser - result");
        }

        // refToken

        // return result;
    }
    


}