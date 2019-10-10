package com.dudaev.khalid.sgrammar.lib.syntax;


public class Token {
    
    private String type = "EOF";
    private String name = "EOF";
    // private String content = "\0";
    private String content = "$";
    private int line;
    private int column;
    private int lenght;
    private boolean isParse;
    // private int setLexemLenght;

    public Token() {

    }
    
    /**
     * 
     * @param type
     * @param name
     * @param content
     * @param line
     * @param column
     * @param lenght
     * @param isParse
     */
    public Token(String type, String name, String content, int line, int column, int lenght, boolean isParse) {
        this.type = type;
        this.name = name;
        this.content = content;
        this.line = line;
        this.column = column;
        this.lenght = lenght;
        this.isParse = isParse;
    }

    public boolean match(String subrule){
        if(type.equals(subrule)) return true;
        return false;
    }

    public String toString(){
        return name + ": " +  content + "\n\t type:\t\t" + type + "\n\t line:\t\t" + line + "\n\t column:\t" + column + "\n\t lenght:\t" + lenght + "\n\t isParse:\t" + isParse + "\n";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public boolean isParse() {
        return isParse;
    }

    public void onParse() { this.isParse = true; }
    public void noParse() { this.isParse = false; }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    
}

