package com.dudaev.khalid.sgrammar.lib;

import com.dudaev.khalid.sgrammar.lib.grammar.GToken;
import com.dudaev.khalid.sgrammar.lib.grammar.GTokens;
import com.dudaev.khalid.sgrammar.lib.syntax.Token;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//interface LexerActionInterface {
//    String code(String name);
//}

public class Lexer {

    private GTokens tokens;

    private ArrayList<Token> tokensArr                  = new ArrayList<>(); //    private static HashMap lexeme = new HashMap<>();
    private HashMap<String, Object> lexemesMap          = new HashMap<>();

    private int pos                                     = 0;
    private String pattern;
    private Object action;

    public int line                                     = 1;
    public int column                                   = 1;
    public int lexemeLength                              = 0;
    public boolean isParse                              = true;

    public Lexer() {

    }

    public void setTokenPatterns(GTokens tokens) {
        this.tokens = tokens;
    }
    
    public ArrayList<Token> getTokens(){
        return this.tokensArr;
    }

    public GToken peek(int relative) {
        String name                                         = tokens.list().get(pos + relative);
        return tokens.get(name);
    }
    
    public void resetPos() {
        pos = 0;
    }

    public GToken nextLexeme() {
        GToken token;
        if(hasNext()) {
            token = peek(0);
            pos++;
            return token;
        }
        System.err.println("No has next lexeme");
        throw new ArrayIndexOutOfBoundsException();
    }

    public boolean hasNext() {
        if(pos + 1 < tokens.list().size()) {
            return true;
        } else pos = 0;
        return false;
    }
    
    public void setAction(Object ac) {
        action = ac;
    }
    
    public void setPos(int pos) {
        this.pos = pos;
    }
    
    // public ArrayList<Token> optimize (ArrayList<Token> tokensArr) {
        
    //     ArrayList<Token> newTokens      = new ArrayList<>();
    //     Iterator iter                   = tokensArr.iterator();
    //     Token tt;
        
    //     while(iter.hasNext()) {
    //         tt = (Token) iter.next();
    //         if(tt.isParse) newTokens.add(tt);
    //     }
        
    //     ListIterator newTokensIter                 = newTokens.listIterator();

    //     while(newTokensIter.hasNext()) {
    //         tt = (Token) newTokensIter.next();

            
    //         if(tt.name.equals("keyword")) continue;
    //         if (tt.type.equals("ident")
    //             && newTokens.get(newTokensIter.nextIndex()).name.equals("lparen") 
    //             && !newTokens.get(newTokensIter.previousIndex() - 1).name.equals("keyword")
    //         ) {
    //             tt.name = "func";
    //             tt.type = "func";
    //         }

    //     }
        
    //     return newTokens;
    // }
    
    public ArrayList<Token> scan (String prg) {
        String pattern          = makePattern();
        Matcher m               = Pattern.compile(pattern).matcher(prg);
        GToken lexemeCollect;
        String name;
        Token token;

        pos = 0;

        while (m.find()) {
            while (hasNext()) {
                lexemeCollect                                        = nextLexeme();
                name                                                = (String) lexemeCollect.getName();
                String lexeme                                        = m.group(name);
                boolean methodExists                                = false;

                if (lexeme != null) {

                    if(name.equals("nline")) {
                        line ++;
                        column = 1;
                    }
                    
                    token = new Token();

                    token.setName(name);
                    token.setLine(line);
                    token.setColumn(column);
                    token.setContent(lexeme);
                    token.onParse();
                    token.setLength(lexeme.length());
                    token.setType(name);
                    
                    Token retToken = runAction(lexemeCollect.getAction(), token, m);

                    if(retToken != null){
                        token = retToken;
                        methodExists = true;
                    }
                    
                    if (!methodExists) {
                        column += lexeme.length();
                    }
                    
                    lexemesMap.put(name, token);
                    tokensArr.add(token);
                }
            }
        }
        return tokensArr;
    }

    private Token runAction(String actionName, Token token, Matcher m) {
        Method meth = null;
        Token retToken = null;

        try {
            meth = action.getClass().getDeclaredMethod( actionName, Token.class, Matcher.class );
            try {
                retToken = (Token) meth.invoke(action, token, m);
                column                  += lexemeLength;
                retToken.setLength(lexemeLength);
                isParse                 = true;
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NullPointerException e) {
                // methodExists                                    = false;
            }
        } catch (NullPointerException | SecurityException | NoSuchMethodException e) {  }

        return retToken;
    }
    
    public String makePattern() {
        String name;
        String patternStart                                     = "(?m)(?<TOKEN>NOMATCH";
        String patternBody                                      = "";
        String patternEnd                                       = ")";
        GToken lexeme;

        pattern = patternStart;
        
        while (hasNext()) {
            lexeme                                               = nextLexeme();
            name                                                = (String) lexeme.getName();
            patternBody                                         = (String) lexeme.getPattern(); 
            pattern                                             += "|(?<" + name + ">" + patternBody + ")";  
        }
        
        pattern += patternEnd;

        return pattern;
    }
    
    public String getPattern() {
        return pattern;
    }

    public GTokens gtoken(){
        return tokens;
    }
    
    // @SuppressWarnings("unchecked")
    // private static Map<String, Integer> getNamedGroups(Pattern regex)
    //     throws NoSuchMethodException, SecurityException,
    //            IllegalAccessException, IllegalArgumentException,
    //            InvocationTargetException {
  
    //   Method namedGroupsMethod = Pattern.class.getDeclaredMethod("namedGroups");
    //   namedGroupsMethod.setAccessible(true);
  
    //   Map<String, Integer> namedGroups = null;
    //   namedGroups = (Map<String, Integer>) namedGroupsMethod.invoke(regex);
  
    //   if (namedGroups == null) {
    //     throw new InternalError();
    //   }
  
    //   return Collections.unmodifiableMap(namedGroups);
    // }
    
}
