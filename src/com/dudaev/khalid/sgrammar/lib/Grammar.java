package com.dudaev.khalid.sgrammar.lib;

import com.dudaev.khalid.sgrammar.lib.grammar.GTokens;
import com.dudaev.khalid.sgrammar.lib.grammar.GToken;

import com.dudaev.khalid.sgrammar.lib.grammar.GNode;
import com.dudaev.khalid.sgrammar.lib.grammar.GProduction;
import com.dudaev.khalid.sgrammar.lib.grammar.GRule;
import com.dudaev.khalid.sgrammar.lib.grammar.GRules;
import com.dudaev.khalid.sgrammar.lib.grammar.Table;
// import com.dudaev.khalid.sgrammar.lib.grammar.TableFIRST;
// import com.dudaev.khalid.sgrammar.lib.grammar.TableFOLLOW;
import com.dudaev.khalid.sgrammar.lib.grammar.TableSYNTAX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grammar {
    
    private GTokens tokens                          = new GTokens();
    private GRules rules                            = new GRules();
    private String tokensPattern                    = "";
    
    private ArrayList<String[]>     tokensList      = new ArrayList<>();
    
    private Table table;
    // private HashMap
    //     <String,
    //      HashMap
    //         <String,
    //                 GProduction>> tableSYNTAX       = new HashMap<>();
    private TableSYNTAX tableSYNTAX;
    
    private HashMap<String, String> tableFIRST      = new HashMap<>();
    private HashMap<String, String> tableFOLLOW     = new HashMap<>();

    // private String tokenName;

//    String tokensFIRST     = "";
    
    public void make (String grammar){
        makeTABLES(grammar);
    }

    private void makeTABLES(String grammar){

//        grammar.replaceAll("(?<comment>\\/\\/.*?)", "");
//        grammar             = grammar.replaceAll("(?:\\s*//.*;)", "");
        grammar                 = grammar.replaceAll("(?s)(?:\\s*//.*?\\n)", "\n");
//        Matcher matchTokens = Pattern.compile("(?s)TOKEN:(?<TOKEN>\\s*\\n(?:\\s+\\w+\\s*\\|\\s*.*?;)+)").matcher(grammar);
//        Matcher matchRules  = Pattern.compile("(?s)SYNTAX:(?<SYNTAX>\\s*\\n(?:\\s+\\w+\\s*\\|\\s*.*?;)+)").matcher(grammar);
//        matchTokens.find();
//        matchRules.find();
//        tokens              = matchTokens.group("TOKEN");
//        rules               = matchRules.group("SYNTAX");

        
        // Matcher matchGrammar  = Pattern.compile("(?s)TOKEN:(?<TOKEN>\\s*\\n(?:\\s+\\w+\\s*\\|\\s*.*?;)+)|SYNTAX:(?<SYNTAX>\\s*\\n(?:\\s+\\w+\\s*\\|\\s*.*?;)+)").matcher(grammar);
        Matcher matchGrammar    = Pattern.compile("(?s)TOKEN:(?<TOKEN>\\s*\\n(?:\\s+\\w+\\s*\\|\\s*.*?\\n)+)|SYNTAX:(?<SYNTAX>\\s*\\n(?:.*?(?:\\s*\\|\\s*.*?\\;)+)+)").matcher(grammar);
        
        matchGrammar.find();
        String tokens           = matchGrammar.group("TOKEN");
        matchGrammar.find();
        String rules            = matchGrammar.group("SYNTAX");

        parseTokens(tokens);
        parseRules(rules);

        table             = new Table(this.tokens, this.rules);

        tableFIRST              = table.getFIRST();
        tableFOLLOW             = table.getFOLLOW();
        tableSYNTAX             = table.getSYNTAX();

        // System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");
        // System.out.println(this.tokens);
        // System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");
        
        // System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");
        // System.out.println(this.rules);
        // System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");


//         System.out.println("----------------------------");
        
//         for (GRule grule : this.rules.list()) {
//         // for (String name : tableFIRST.keySet()){
//             System.out.println(grule.getName() + "\t\t\t: " + tableFIRST.get(grule.getName()));
//         }

//         // System.out.println(tableFIRST.toString());

//         System.out.println("----------------------------");

//         for (GRule rule : this.rules.list()) {
//             if (tableFOLLOW.containsKey(rule.getName())) System.out.println(rule.getName() + "\t\t\t: " + tableFOLLOW.get(rule.getName()));
//         }

//         // for (String name : tableFOLLOW.keySet()) {
//         //     System.out.println(name + "\t\t\t: " + tableFOLLOW.get(name));
//         // }

//         System.out.println("----------------------------");

// // //        getIntersect(tableFIRST.get("ERR"), tableFOLLOW.get("F"));

//         System.out.println("");

//         System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");
//         System.out.print("\t|");

//         for (String tokenName : this.tokens.list()) {
//             System.out.print(String.format("%19s|", tokenName));
//         }

//         // System.out.print("  $      \t|");

//         System.out.println();
//         System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");

//         for( GRule ruleName : this.rules.list() ){

//             System.out.print(ruleName.getName() + "\t|");

//             for (String tokenName : this.tokens.list()) {
// //                System.out.println("\t " + tokenName[0] + "");
//                 if(tableSYNTAX.getTable().containsKey(tokenName))
//                     // System.out.print("  " + tableSYNTAX.getTable().get(tokenName).get(ruleName.getName()) + "\t\t|");
//                     System.out.print(String.format("%19s|", tableSYNTAX.getTable().get(tokenName).get(ruleName.getName())));
//                 else System.out.print("  " + null + "\t\t|");
//             }

//             System.out.println();
//         }

//         System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");

//         System.out.println("");
        
    }

    /**
     * Parse tokens from file
     * @param tokensText
     */
    private void parseTokens(String tokensText) {
        Matcher mTokens                      = Pattern.compile("(?s)(?<nametoken>\\w+)\\s*\\|\\s*(?<pattern>.*?)(?:\\s*\\&(?<actiontoken>\\w+))?\\;").matcher(tokensText);

        // String or = "";

        // if(!tokensPattern.matches("")) or = "|";

        while (mTokens.find()) {
            
            String nametoken                = mTokens.group("nametoken");
            String pattern                  = mTokens.group("pattern");
            String actiontoken              = mTokens.group("actiontoken");
            String action                   = null;

            pattern                         = pattern.trim();
            
            tokens.add(nametoken, new GToken(nametoken, pattern, actiontoken));
            tokensPattern += "|(?<" + nametoken + ">" + pattern + ")";
        }

        tokensPattern = tokensPattern.replaceFirst("\\|", "");
    }

    /**
     * Parse rules from file
     * @param rulesText
     */
    private void parseRules(String rulesText) {

        // System.out.println("RULE TXT: " + rulesText);

        String rulesListPattern             = "";
        Matcher mRulesList                  = Pattern.compile("(?m)^\\s*(?<name>\\w+)(?:\\s*\\&\\w+)?\\s*\\|").matcher(rulesText);

        while(mRulesList.find()) { 
            rulesListPattern       += "|" + mRulesList.group("name");
        }

        rulesListPattern = rulesListPattern.replaceFirst("\\|", "");

        Matcher mRules                      = Pattern.compile("(?s)(?<nameRule>\\w+)(?:\\s*\\&(?<actionRule>\\w+))?\\s*\\|\\s*(?<prods>.*?)\\;").matcher(rulesText);

        while (mRules.find()) {
            
            String nameRule                 = mRules.group("nameRule");
            String actionRule               = mRules.group("actionRule");
            String prods                    = mRules.group("prods");
            String action                   = null;
            ArrayList<GProduction> prodArr   = new ArrayList<>();
            
            prods                           = prods.trim();

            
            for( String prod : prods.split("\\s*\\|\\s*")){
                
                
                ArrayList<GNode> nodes = new ArrayList<>();
                
                prod            = prod.trim();
                Matcher mProds  = Pattern.compile("(?s)\\&\\b(?<actionProd>\\w+)\\b").matcher(prod);
                
                if(mProds.find()) {
                    action  = mProds.group("actionProd");
                    prod    = mProds.replaceFirst("");
                    prod    = prod.trim();
                    // System.out.println("RULE PROD: " + action);
                }

                for(String node : prod.split("\\s+")) {
                    nodes.add(new GNode(node, nameRule, action, !node.matches(rulesListPattern)));
                }

                prodArr.add(new GProduction(nodes, action));
            }
            
            rules.add(nameRule, new GRule(nameRule, actionRule, prodArr));
        }
    }

    public GTokens getGTokens(){
        return tokens;
    }

    public String getTokensPattern(){
        return tokensPattern;
    }

    public Table getTable() {
        return table;
    }

    public GRules getRules() {
        return rules;
    }

}
