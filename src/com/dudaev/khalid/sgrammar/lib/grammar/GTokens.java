package com.dudaev.khalid.sgrammar.lib.grammar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Tokens
 */
public class GTokens {

    private HashMap<String, GToken> tokensMap  = new HashMap<>();
    private ArrayList<String> tokenList      = new ArrayList<>();
    private ArrayList<GToken> listGTokens    = new ArrayList<>();

    public void add(String tokenName, GToken token) {
        tokenList.add(tokenName);
        listGTokens.add(token);
        tokensMap.put(tokenName, token);
    }

    public GToken get(String tokenName) {
        return tokensMap.get(tokenName);
    }

    public ArrayList<String> list() {
        return tokenList;
    }

    public ArrayList<GToken> listGTokens() {
        return listGTokens;
    }

    public boolean isExists(String tokenName){
        return tokensMap.containsKey(tokenName);
    }

    @Override
    public String toString() {
        return "Tokens [tokenList=" + tokenList + ", tokensMap=" + tokensMap + "]";
    }

    
    
}