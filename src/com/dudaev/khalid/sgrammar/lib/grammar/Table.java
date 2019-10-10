package com.dudaev.khalid.sgrammar.lib.grammar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Table
 */
public class Table {

    HashMap<String, String> tableFIRST      = new HashMap<>();
    HashMap<String, String> tableFOLLOW     = new HashMap<>();
    TableSYNTAX tableSYNTAX;
    GTokens tokens;
    GRules rules;

    public Table(GTokens tokens, GRules rules) {
        this.tokens      = tokens;
        this.rules      = rules;

        tableFIRST      = new TableFIRST(rules).getTable();
        tableFOLLOW     = new TableFOLLOW(tableFIRST, rules).getTable();
        tableSYNTAX     = new TableSYNTAX(tokens, rules, tableFIRST, tableFOLLOW);

        checkingERR();
    }

    public HashMap<String, String> getFIRST() {
        return tableFIRST;
    }

    public HashMap<String, String> getFOLLOW() {
        return tableFOLLOW;
    }

    public TableSYNTAX getSYNTAX() {
        return tableSYNTAX;
    }

    void checkingERR() {
        String rulesList = null;
        ArrayList<GProduction> prods = new ArrayList<>();
        String firstA;
        String firstB;        
        String intersectToken = null;
        String errorBuffer = "";
        
        for(GRule ruleName: rules.list()){
            rulesList   = rules.get(ruleName.getName()).getName();
            prods     = rules.get(ruleName.getName()).getProductions();

            if(tableFOLLOW.get(ruleName.getName()) == null) {
                errorBuffer += "\033[91m\033[91mERROR!\033[0m\033[0m The token \033[95m'" + ruleName.getName() + "'\033[0m has never been used\n";
            }

            for (int i = 0; i < prods.size() - 1; i++) {
                firstA = prods.get(i).getNodes().get(0).getNode();
                for (int ii = i + 1; ii < prods.size(); ii++) {
                    firstB = prods.get(ii).getNodes().get(0).getNode();
                    if(tableFIRST.containsKey(firstA) && tableFIRST.containsKey(firstB)) {

                        if(isContainsEPSYLON(tableFIRST.get(firstA))) {
                            intersectToken = getIntersect(tableFIRST.get(firstB), tableFOLLOW.get(ruleName.getName()));
                            if(intersectToken != null) errorBuffer += "\033[91mERROR!\033[0m The token \033[95m'" + intersectToken + "\033[0m' from \033[95m'" + firstB + "', '" + ruleName.getName() + "'\033[0m intersect each other in " + ruleName.getName() + "\n";
                        }

                        if(isContainsEPSYLON(tableFIRST.get(firstA))) {
                            intersectToken = getIntersect(tableFIRST.get(firstA), tableFOLLOW.get(ruleName.getName()));
                            if(intersectToken != null) errorBuffer += "\033[91mERROR!\033[0m The token \033[95m'" + intersectToken + "\033[0m' from \033[95m'" + firstA + "', '" + ruleName.getName() + "'\033[0m intersect each other in " + ruleName.getName() + "\n";
                        }

                        intersectToken = getIntersect(tableFIRST.get(firstA), tableFIRST.get(firstB));
                        
                        if (intersectToken != null) {
                            errorBuffer += "\033[91mERROR!\033[0m The token \033[95m'" + intersectToken + "\033[0m' from \033[95m'" + firstA + "', '" + firstB + "'\033[0m intersect each other in '" + ruleName.getName() + "'" + "\n";
                        }
                    }
                }
            }
        }

        if(errorBuffer != "") {
            System.out.println(errorBuffer);
            throw new Error();
        }

    }
    
    /**
     * 
     * @param rule
     * @return
     */
    boolean isContainsEPSYLON(String rule){       
        return rule.matches(".*?\\be\\b.*?");
    }

    /**
     * 
     * @param matchA
     * @param matchB
     * @return
     */
    String getIntersect(String matchA, String matchB){

        String res = null;
        String commma = "";

        for(String prod : matchA.split("\\s*\\|\\s*")){
            if(prod.matches(matchB)) {
                if(res == null) res = "";
                res += commma + prod;
                commma = ", ";
            }
        }

        return res;
    }

}