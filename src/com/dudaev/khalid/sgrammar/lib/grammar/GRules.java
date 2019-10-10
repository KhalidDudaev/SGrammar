package com.dudaev.khalid.sgrammar.lib.grammar;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * This class contains rules as list of names and hashmap which contains keys as names of rules and objects of rules as values.
 * Rules
 */
public class GRules {

    private HashMap<String, GRule> rulesMap  = new HashMap<>();
    private ArrayList<GRule> ruleList      = new ArrayList<>();

    public void add(String ruleName, GRule rule) {
        ruleList.add(rule);
        rulesMap.put(ruleName, rule);
    }

    public GRule get(String ruleName) {
        return rulesMap.get(ruleName);
    }

    public ArrayList<GRule> list() {
        return ruleList;
    }

    public boolean isExists(String rule){
        return rulesMap.containsKey(rule);
    }

    @Override
    public String toString() {
        return "Rules : { ruleList : " + ruleList + ",\n rulesMap : " + rulesMap + " }";
    }

}

