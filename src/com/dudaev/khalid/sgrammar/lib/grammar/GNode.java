package com.dudaev.khalid.sgrammar.lib.grammar;

/**
 * This class contains data of node as name and boolean value for checking current node is a terminal or not.
 * Node
 */
public class GNode {

    String node;
    String rule;
    String action;
    boolean isTERMINAL = false;

    public GNode(String node, String rule, String action, boolean isTERMINAL) {
        this.node = node;
        this.rule = rule;
        this.action = action;
        this.isTERMINAL = isTERMINAL;
    }

    public String getNode() {
        return node;
    }

    public String getRule() {
        return rule;
    }

    public String getAction() {
        return action;
    }

    // public void setNode(String node) {
    //     this.node = node;
    // }

    public boolean isTERMINAL() {
        return isTERMINAL;
    }

    @Override
    public String toString() {
        // return "Node : { isTERMINAL : " + isTERMINAL + ", node : " + node + " }";
        return node;
    }

    // public void setTERMINAL(boolean isTERMINAL) {
    //     this.isTERMINAL = isTERMINAL;
    // }

}