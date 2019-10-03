package com.dudaev.khalid.sgrammar.lib.grammar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * TableSYNTAX
 */
public class TableSYNTAX {
    
    GTokens tokens;
    GRules rules;

    HashMap<String, String> tableFIRST      = new HashMap<>();
    HashMap<String, String> tableFOLLOW     = new HashMap<>();


    HashMap
    <String,
     HashMap
        <String,
                GProduction>>       tableSYNTAX     = new HashMap<>();

    public TableSYNTAX(GTokens tokens, GRules rules, HashMap<String, String> tableFIRST, HashMap<String, String> tableFOLLOW) {
        this.tokens = tokens;
        this.tableFIRST = tableFIRST;
        this.tableFOLLOW = tableFOLLOW;
        this.rules = rules;

        makeSyntaxTABLE();
    }
         
    void makeSyntaxTABLE(){

        ArrayList<String> tkList = tokens.list();
        String followTokens;
        String firstTokens;
        ArrayList<GProduction> prods = new ArrayList<>();
        String currProd = "";

        tkList.add("$");

        for(String token : tkList) {
            tableSYNTAX.put(token, new HashMap<String, GProduction>() {{
                for(GRule ruleName : rules.list()) {
                    put(ruleName.getName(), null);
                }
            }});
        }

        for(GRule ruleName : rules.list()) {
            
            followTokens = tableFOLLOW.get(ruleName.getName());
            prods = rules.get(ruleName.getName()).getProductions();
            currProd = ruleName.getName();
            firstTokens = tableFIRST.get(ruleName.getName());

            // System.out.println("### " + ruleName.getName());

            for(GProduction prod: prods){
                
                String token = prod.getNodes().get(0).getNode();
                
                for(String fToken : firstTokens.split("\\|")) {

                    if( token.equals("e") && fToken.equals("e") ){
                        for (String follow : followTokens.split("\\s*\\|\\s*")) {
                            tableSYNTAX.get(follow).put(ruleName.getName(), prod); // R -> e
                        }
                    } else if( token.equals(fToken)){
                        tableSYNTAX.get(fToken).put(ruleName.getName(), prod);
                    } else if( tableFIRST.containsKey(token) && fToken.matches(tableFIRST.get(token)) ){
                        tableSYNTAX.get(fToken).put(ruleName.getName(), prod);
                    }

                }
            }
        }
    }

    public HashMap<String, HashMap<String, GProduction>> getTable() {
        return tableSYNTAX;
    }

    public GProduction getProduction(String gToken, String gRule){

        return tableSYNTAX.get(gToken).get(gRule);
    }

    // public void setTableSYNTAX(HashMap<String, HashMap<String, Production>> tableSYNTAX) {
    //     this.tableSYNTAX = tableSYNTAX;
    // }



}