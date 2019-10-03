package com.dudaev.khalid.sgrammar.lib.grammar;

import java.util.ArrayList;

/**
 * This class contains production as list of node objects.
 * Production
 */
public class GProduction {

    // private String name;
    private ArrayList<GNode> production;
    private String action;
    // private ArrayList<String> nodes = new ArrayList<>();
    // private HashMap<String, Token> tokensMap = new HashMap<>();

    public GProduction(ArrayList<GNode> production, String action) {
        // this.name = name;
        this.production     = production;
        this.action         = action;

        // for(String node : production.split("\\s+")) {
        //     this.nodes.add(node);
        // }

    }

    public ArrayList<GNode> getProds() {
        return production;
    }

    // public void setName(String name) {
    //     this.name = name;
    // }

    // public String[] getProd() {
    //     return production;
    // }

    // public void setProd(String[] production) {
    //     this.production = production;
    // }

    public String getAction() {
        return action;
    }

    @Override
    public String toString() {
        // return "Production { action : " + action + ", production : " + production + " }";
        return production.toString();
    }

    public ArrayList<GNode> getNodes() {
        return production;
    }

    public boolean contains(GNode node) {
       return production.contains(node);
    }

    // public void setNodes(ArrayList<String> nodes) {
    //     this.nodes = nodes;
    // }

    // public void setAction(HashMap<String, String> action) {
    //     this.action = action;
    // }

    

    

}