package com.dudaev.khalid.sgrammar.lib.grammar;

import java.util.ArrayList;

/** 
 * This class contains data for rule object as name, action and array list of productions.
 * Rule
 */
public class GRule {

    private String name;
    private GNode gnode;
    private String action;
    private ArrayList<GProduction> productions = new ArrayList<>();

    public GRule(String name, String action, ArrayList<GProduction> prod) {
        this.name = name;
        this.action = action;
        this.gnode = new GNode(name, name, action, false);
        this.productions = prod;
    }

    public String getName() {
        return name;
    }

    public String getAction() {
        return action;
    }

    public GNode getNode() {
        return gnode;
    }

    public ArrayList<GProduction> getProductions() {
        return productions;
    }

    @Override
    public String toString() {
        return "Rule : { action : " + action + ", name : " + name + ", prod : " + productions + " }\n";
    }

}