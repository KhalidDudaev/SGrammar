package com.dudaev.khalid.sgrammar.lib.syntax;


public class Token {
    
    public String name = "EOF";
    // public String content = "\0";
    public String content = "$";
    public int line;
    public int column;
    public int lenght;
    public boolean isParse;
    // public int setLexemLenght;
    public String type = "EOF";
    
    public boolean match(String subrule){
        if(type.equals(subrule)) return true;
        return false;
    }

    

    public String toString(){
        return name + ": " +  content + "\n\t type:\t\t" + type + "\n\t line:\t\t" + line + "\n\t column:\t" + column + "\n\t lenght:\t" + lenght + "\n\t isParse:\t" + isParse + "\n";
    }
    
}

