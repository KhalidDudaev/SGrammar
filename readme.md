# Simple Grammar Library

## Description

> ##### This is a simple grammar library for a selected language or phrase based on LL(1).

## Usage in code

Let's look at a simple mathematical expression:

#### ``` 41 + 4 * 9 / (3 + 6) + 55 ```

This expression have the math operators with priorities and also it has parentheses. For this we can make next grammar.
Step 1. We must define tokens for our grammar. It is be:
- left and right parenthes
- number
- maths operators
- spaces
- ...and last token for detect error.

## Lexical part

Each rule begins with the name of the rule and after the '|' need to write a regular expression pattern for Lexer, which is designed to search in the input string for matching this token. After, if there is, we can write the name of the method that will perform the additional action. The method name has a '&' character as a prefix. Methods for additional actions are in the class that we are sending as a parameter to Lexer. Each rule ends with a ';'. Rules can also contain comments that begin with "//" and continue to the end of the line. Its looks like this:

```scss
// some comment...
tokenname | \bpattern\b &methodname ; // some commnet...
```

This grammar contains an 'error' token. When Lexer for the next token does not find a match with each pattern above except 'error', then this token does not belong to this grammar, then there remains a match with 'error'. It remains only to add a method to handle this situation.

Also have the token 'space' for spaces. The spaces for our grammar not significant and they not needs for the Syntax parser. Need to exclude spaces from the following Syntax parsing. To do this, create a class for additional actions and create a method in this class. All methods should use the Token and Matcher parameter and return Token.

``` java
public class ParserLexer {
    Parser parser;

    public ParserLexer () {}

    public ParserLexer (Parser parent) {
        this.parser = parent;
    }

    public Token space (Token token, Matcher m) {
        token.setLexemLenght = 0;
        token.isParse = false;
        return token;
    }
}
```

So that we can write this as:
```scss
lparen  | \(;
rparen  | \);
number  | (?:(?<!(?:\=|\/|\*|\-|\+)\s*)?(?:\-?\d+(?:\.\d+)?(?:[eE]\-?\d+)?)\b);
opadd   | \+;
opsub   | \-;
opmul   | \*;
opdiv   | \/;
oppower | \^;
space   | (?:(?!\r?\n)\s)+  &space ;
error   | .                 &error ;
```

## Syntax part

Each rule for syntax begins with the name of the rule and after it is one or more productions. Each production begins with '|". After, if there is, we can write the name of the method that will perform the additional action as for rule also for production. The rule ends with a ';'. For example:

```scss
// rule name                                method name for this production 
//    |   method name for this rule            |
//    |      |                                 |
expression &exp | operand operator operand &expprod1 ;

operator &op
| plus &opplus
| minus &opminus ;
```

Now we will write the rules for the expression, which was discussed above.
```scss
    E   | T E_;

    E_  | AS T E_
        | e;

    T   | P T_;

    T_  | MD P T_
        | e;

    P   | F P_;

    P_  | oppower F P_      &opPOW
        | e;

    AS  | opadd             &opADD
        | opsub             &opSUB ;

    MD  | opmul             &opMUL
        | opdiv             &opDIV ;

    F   | lparen E rparen   &paren
        | number            &number
        | ident             &ident ;
```
There are terminals and non-terminals in this grammar. We designated nonterminals in capital letters, and terminals in token names from lexical rules. In this grammar contain the special token 'e' for designated empty set.

### To be continued ...