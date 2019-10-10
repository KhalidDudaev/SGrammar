package com.dudaev.khalid.sgrammar.lib.grammar;

import java.util.HashMap;

/**
 * TableFIRST
 */
public class TableFIRST {

    HashMap<String, String> tableFIRST      = new HashMap<>();
    GRules rules                             = new GRules();

    public TableFIRST(GRules rules) {
        this.rules = rules;
        makeFIRST();
    }

    void makeFIRST(){
        String collFIRST;
        for (GRule ruleName : rules.list()){
            collFIRST    = getFIRST(ruleName.getName());
            collFIRST    = collFIRST.replaceFirst("\\|", "");
            tableFIRST.put(ruleName.getName(), collFIRST);
        }
    }
    
    /**
     * 
     * @param ruleName
     * @return
     */
    String getFIRST(String ruleName){

        String first;
        String tokensFIRST          = "";
        
        if(this.rules.isExists(ruleName)) {
            for(GProduction prod : this.rules.get(ruleName).getProductions()) {
                GNode node   = prod.getNodes().get(0);
                first       = node.getNode();
                if(!node.isTERMINAL()) {
                // if(this.rules.isExists(first)) {
                    tokensFIRST = tokensFIRST + getFIRST(first);
                } else {
                    tokensFIRST = tokensFIRST + "|" + first;
                }
            }
        }

        return tokensFIRST;
    }

    public HashMap<String, String> getTable() {
        return tableFIRST;
    }

    @Override
    public String toString() {

        String buff = "";

        for (GRule name : this.rules.list()) {
            buff += name.getName() + String.format("%10s| ", tableFIRST.get(name.getName())) + "\n";
        }

        return buff;
    }

}