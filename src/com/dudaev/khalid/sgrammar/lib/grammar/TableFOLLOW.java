package com.dudaev.khalid.sgrammar.lib.grammar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * TableFOLLOW
 */
public class TableFOLLOW {

    HashMap<String, String> tableFIRST      = new HashMap<>();
    HashMap<String, String> tableFOLLOW     = new HashMap<>();
    GRules rules                             = new GRules();


    public TableFOLLOW(HashMap<String, String> tableFIRST, GRules rules) {
        this.tableFIRST = tableFIRST;
        this.rules = rules;
        makeFOLLOW();
    }

    void makeFOLLOW() {
        
        String follow       = "";
        String[] prods;
        String collFOLLOW   = "";
        boolean comp        = false;
        boolean start       = true;

        for (GRule ruleName : rules.list()) {
            collFOLLOW = "";
            comp = false;
            
            follow = getFOLLOW(ruleName.getName());

            if(!collFOLLOW.equals("")) collFOLLOW += "|"  + follow;
            else collFOLLOW = follow;

            if(start) collFOLLOW += "|$";
            
            tableFOLLOW.put(ruleName.getName(), collFOLLOW);

            start = false;
        }
    }

    /**
     * 
     * @param searchFollowRule
     * @return
     */
    String getFOLLOW(String searchFollowRule){
//        String rules        = rulesMap.get(ruleName);
        // String follow;
        String tokensFOLLOW     = null;
        String token;
        
        if(rules == null) return null;

        for(GRule ruleName : rules.list()){
            for(GProduction prod : rules.get(ruleName.getName()).getProductions()) {
                // String[] tokens = new String[prod.getNodes().size()];
                // tokens = prod.getNodes().toArray(tokens);
                ArrayList<GNode> tokens =  prod.getNodes();

                for ( int it = 0; it < tokens.size(); it++) {
                    token = tokens.get(it).getNode();
                    if ( searchFollowRule.equals(token) ) {
                        if( it < tokens.size() - 1 ) {
                            if(tableFIRST.containsKey(tokens.get(it + 1).getNode())) {
                                tokensFOLLOW = tableFIRST.get(tokens.get(it + 1).getNode()).replaceAll("\\|\\be\\b","") + "|" + tableFOLLOW.get(ruleName.getName());
                            } else {
                                tokensFOLLOW = tokens.get(it + 1).getNode();
                            }
                        } else {
                            if(tableFOLLOW.containsKey(ruleName.getName())) {
//                                System.out.println("### " + ruleName);
                                tokensFOLLOW = tableFOLLOW.get(ruleName.getName());
                            }
                        }
                    } else continue;
                }
            }
        }

        return tokensFOLLOW;
    }

    public HashMap<String, String> getTable() {
        return tableFOLLOW;
    }

    // public void setTableFOLLOW(HashMap<String, String> tableFOLLOW) {
    //     this.tableFOLLOW = tableFOLLOW;
    // }
   
}