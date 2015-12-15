package com.java.levelp.level2.lesson9;

import java.math.BigDecimal;
import java.math.MathContext;

public class MathParser {
     /*
    Grammar:
    1) E -> E + T
    2) E -> E - T
    3) E -> T
    4) E -> -E
    5) T -> T * F
    6) T -> T / F
    7) T -> F
    8) F -> (E)
    9) E -> E^P
    10) P -> E
    5*6
    5-4+6-7
    3-4*6+7/9
     */

    private String exp;
    private int pos = 0;

    public MathParser(String exp) {
        this.exp = exp.replaceAll(" |/n", "");
    }

    public BigDecimal parse() {
        return expression(term(factor()));
    }

    private BigDecimal factor() {
        boolean sin = false;

        char c = getChar();
        boolean unaryMinus = false;
        if (c == '-'){
            unaryMinus = checkUnaryMinis();
            if (unaryMinus)
                c = getChar();
        }

        if (exp.contains("sin") && exp.length() - pos >= 6) { //sin expression contains min 6 symbols
            sin = checkSin(pos - 1);
            if (sin) {
                pos += 2;
//                if (unaryMinus)
//                    pos++;
                c = getChar();
            }
        }

        if (Character.isDigit(c)) {
            BigDecimal b;
            if (unaryMinus) {
                return getValue().multiply(new BigDecimal(-1));
            }
            else
                return getValue();
        } else if (c == '(') {
            if (unaryMinus){
                if (sin)
                    return new BigDecimal(Math.sin(new MathParser(getExpression()).parse().doubleValue())).multiply(new BigDecimal(-1));
                return new MathParser(getExpression()).parse().multiply(new BigDecimal(-1));
            }
            else {
                if (sin)
                        return new BigDecimal(Math.sin(new MathParser(getExpression()).parse().doubleValue()));
                return new MathParser(getExpression()).parse();
            }
        }
        return new BigDecimal(0);
    }

    private BigDecimal term(BigDecimal left) {
        char c = getChar();
        if (c == '^'){
            return term(new BigDecimal(Math.pow(left.doubleValue(), factor().doubleValue())));
        }
        else if (c == '*') {
            return term(left.multiply(factor()));
        } else if (c == '/') {
            MathContext mc = new MathContext(10);
            return term(left.divide(factor(), mc));
        }
        pos--;
        return left;
    }

    private BigDecimal expression(BigDecimal left) {
        char c = getChar();
        if (c == '+') {
            return expression(left.add(term(factor())));
        } else if (c == '-') {
            return expression(left.subtract(term(factor())));
        }
        pos--;
        return left;
    }

    private char getChar() {
        if (pos < exp.length()) {
            return exp.charAt(pos++);
        }
        return 'e';
    }

    private BigDecimal getValue() {
        char c;
        pos--;
        int dotCount = 0;
        StringBuilder result = new StringBuilder();
        while (Character.isDigit(c = getChar()) || c == '.') {
            if (c == '.'){
                dotCount++;
            }
            if (dotCount == 2){
                throw new IllegalArgumentException();
            }
            result.append(c);
        }
        pos--;
            return BigDecimal.valueOf(Double.parseDouble(result.toString()));
    }

    private String getExpression() {
        char c;
        pos--;
        StringBuilder result = new StringBuilder();
        while ((c = getChar()) != ')') {
            if (c == '(')
                result.append(expression(term(factor())));
            else
                result.append(c);
        }
        return result.toString();
    }

    private boolean checkUnaryMinis(){
        if (exp.charAt(pos) == '(' || exp.charAt(0) == '-'){
            return true;
        }
        else if (pos > 1){
            if (exp.charAt(pos - 2) == '('){
                return true;
            }
        }
        return false;
    }

    private boolean checkSin(int position) {
        if (exp.charAt(position) == 's')
            if (exp.charAt(position + 1) == 'i')
                if (exp.charAt(position + 2) == 'n')
                    return true;
                else throw new IllegalArgumentException();
            else throw new IllegalArgumentException();
        return false;
    }

}
