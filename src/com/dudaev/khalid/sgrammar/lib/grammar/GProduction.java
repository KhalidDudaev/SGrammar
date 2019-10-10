package com.dudaev.khalid.sgrammar.lib.grammar;

import java.util.ArrayList;

/**
 * This class contains production as list of node objects.
 * Production
 */
public class GProduction {

    private ArrayList<GNode> production;
    private String action;

    public GProduction(ArrayList<GNode> production, String action) {
        this.production     = production;
        this.action         = action;
    }

    public ArrayList<GNode> getProds() {
        return production;
    }

    public String getAction() {
        return action;
    }

    @Override
    public String toString() {
        return production.toString();
    }

    public ArrayList<GNode> getNodes() {
        return production;
    }

    public boolean contains(GNode node) {
       return production.contains(node);
    }

}