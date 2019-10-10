package com.dudaev.khalid.sgrammar.lib.grammar;

/**
 * Token
 */
public class GToken {

    private String name;
    private String pattern;
    private String action;

    public GToken(String name, String pattern, String action) {
        this.name = name;
        this.pattern = pattern;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public String getPattern() {
        return pattern;
    }

    public String getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "Token [action=" + action + ", name=" + name + ", pattern=" + pattern + "]";
    }

}