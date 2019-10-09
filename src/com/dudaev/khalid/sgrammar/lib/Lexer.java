package com.dudaev.khalid.sgrammar.lib;

import java.lang.ref.SoftReference;
import java.lang.reflect.GenericSignatureFormatError;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dudaev.khalid.sgrammar.lib.grammar.GToken;
import com.dudaev.khalid.sgrammar.lib.grammar.GTokens;
import com.dudaev.khalid.sgrammar.lib.syntax.Token;

//interface LexerActionInterface {
//    String code(String name);
//}

public class Lexer {


    //##########################################################################################
    private GTokens tokens;
    //##########################################################################################

    //    private static List<HashMap> lexems = new ArrayList<>(); //    private static HashMap lexem = new HashMap<>();
    private ArrayList<Token> tokensArr                  = new ArrayList<>(); //    private static HashMap lexem = new HashMap<>();
    // private ArrayList<String> orderList                 = new ArrayList<>();
    private HashMap<String, Object> lexemsMap           = new HashMap<>();
    private int index                                   = 0;
    private int pos                                     = 0;
    private String pattern;
    private Object action;
    
    // private Iterator iter                               = orderList.iterator();
    // private Iterator iter                               = tokens.list().iterator();
    private Iterator iter;
    public int line                                     = 1;
    public int column                                   = 1;
    public int lexemLenght                              = 0;
    // public int setLexemLenght                           = 0;
    public boolean isParse                              = true;

    
    
    //##########################################################################################

    public Lexer() {

    }

    public void setTokenPatterns(GTokens tokens) {
        this.tokens = tokens;
    }
 
    //##########################################################################################
    
    public ArrayList<Token> getTokens(){
        // ArrayList<Token> t = new ArrayList<>();
        // t.addAll(this.tokensArr);
        // return  t;

        return this.tokensArr;
    }

    public GToken peek(int relative) {
        String name                                         = tokens.list().get(pos + relative);
        return tokens.get(name);
    }
    
    public void resetPos() {
        pos = 0;
    }

    public GToken nextLexem() {
        String name = "";
        GToken token;
        if(hasNext()) {
        // if(hasNext() && peek(0).getName() != "$") {
            token = peek(0);
            pos++;
            return token;
        }
        System.err.println("No has next lexem");
        throw new ArrayIndexOutOfBoundsException();
    }

    public boolean hasNext() {
        if(pos + 1 < tokens.list().size()) {
//            System.out.println("---------- TRUE");
            return true;
        } else pos = 0;
        return false;
    }
    
    public void setAction(Object ac) {
        action = ac;
    }
    
    public void setPos(int pos) {
        index = pos;
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

        // System.out.println(">>>>>>>>>>>>>> " + pattern);
        
        Matcher m               = Pattern.compile(pattern).matcher(prg);
        GToken lexemCollect;
        String name;
//        HashMap<String, Object> tokenMap;
        Token token;

//        System.out.println("PATTERN: " + pattern);
        
        pos = 0;

        
        while (m.find()) {
            while (hasNext()) {
                lexemCollect                                        = nextLexem();
                name                                                = (String) lexemCollect.getName();
//                System.out.println("LEXEM: " + name);
                String lexem                                        = m.group(name);
                boolean methodExists                                = false;

                // m.replaceFirst("");

    
                if (lexem != null) {

                //    try {
                //        System.out.println(getNamedGroups(m.pattern()));
                //    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                //        e.printStackTrace();
                //    } 
        
                    token = new Token();
                    // if (name.equals("error")) System.err.println("error at L" + line + ":C" + column + " for '" + lexem + "'");
        
    //                LexerAction action                                  = new LexerAction(this);

                    if(name.equals("nline")) {
                        line ++;
                        column = 1;
                    }
    
                    lexemLenght                                         = lexem.length();
                    token.name                                          = name;
                    token.line                                          = line;
                    token.column                                        = column;
                    token.content                                       = lexem;
                    token.isParse                                       = isParse;
                    token.lenght                                        = lexemLenght;
                    token.type                                          = name;
                    
                    // System.out.println("___ " + token.content);

                    Token retToken = runAction(lexemCollect.getAction(), token, m);

                    if(retToken != null){
                        token = retToken;
                        methodExists = true;
                    }


                    // Method meth = null;
                    
                    // try {
                    //     meth = action.getClass().getDeclaredMethod(lexemCollect.getAction(), Token.class, Matcher.class);
                    //     try {
                    //         methodExists                                    = true;
                    //         token = (Token) meth.invoke(action, token, m);
                            
                    //         if (setLexemLenght > 0) {
                    //             column                                      += token.setLexemLenght;
                    //             token.lenght                                = token.setLexemLenght;
                    //         } else {
                    //             column                                      += lexemLenght;
                    //             token.lenght                                = lexemLenght;
                    //         }
                    //         isParse                                         = true;
                    //         setLexemLenght                                  = 0;

                    //     } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NullPointerException e) {
                    //         methodExists                                    = false;
                    //     }
                    // } catch (NullPointerException | SecurityException | NoSuchMethodException e) {  }
        
                    // if(name.equals("nline")) {
                    //     line ++;
                    //     column = 1;
                    // }
                    
                    if (!methodExists) {
                        column += lexem.length();
                    }
                    
                    lexemsMap.put(name, token);
                    tokensArr.add(token);
                }
            }
        }

        // tokensArr.add(new Token());
        return tokensArr;
    }

    private Token runAction(String actionName, Token token, Matcher m) {
        Method meth = null;
        boolean methodExists                                = false;
        // ArrayList<Objects> ret = new ArrayList<>();
        Token retToken = null;

        try {
            meth = action.getClass().getDeclaredMethod( actionName, Token.class, Matcher.class );
            try {
                methodExists                                    = true;
                retToken = (Token) meth.invoke(action, token, m);
                
                // if (setLexemLenght > 0) {
                //     column                                      += retToken.setLexemLenght;
                //     retToken.lenght                                = retToken.setLexemLenght;
                // } else {
                //     column                                      += lexemLenght;
                //     retToken.lenght                                = lexemLenght;
                // }

                column                  += lexemLenght;
                retToken.lenght         = lexemLenght;

                isParse                 = true;
                // setLexemLenght          = 0;
                
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NullPointerException e) {
                methodExists                                    = false;
            }
        } catch (NullPointerException | SecurityException | NoSuchMethodException e) {  }

        // refToken

        return retToken;
    }
    
    public String makePattern() {
        String name;
        String patternStart                                     = "(?m)(?<TOKEN>NOMATCH";
        String patternBody                                      = "";
        String patternEnd                                       = ")";
        GToken lexem;
//        String code;
        
        pattern = patternStart;
        
        while (hasNext()) {
            lexem                                               = nextLexem();
            name                                                = (String) lexem.getName();
            patternBody                                         = (String) lexem.getPattern(); 
            pattern                                             += "|(?<" + name + ">" + patternBody + ")";  
        }
        
        pattern += patternEnd;

//        System.out.println("PATTERN: " + pattern);

        return pattern;
    }
    
    public String getPattern() {
        return pattern;
    }

    public GTokens gtoken(){
        return tokens;
    }
    
    @SuppressWarnings("unchecked")
    private static Map<String, Integer> getNamedGroups(Pattern regex)
        throws NoSuchMethodException, SecurityException,
               IllegalAccessException, IllegalArgumentException,
               InvocationTargetException {
  
      Method namedGroupsMethod = Pattern.class.getDeclaredMethod("namedGroups");
      namedGroupsMethod.setAccessible(true);
  
      Map<String, Integer> namedGroups = null;
      namedGroups = (Map<String, Integer>) namedGroupsMethod.invoke(regex);
  
      if (namedGroups == null) {
        throw new InternalError();
      }
  
      return Collections.unmodifiableMap(namedGroups);
    }


    
}
