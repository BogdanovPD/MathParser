package com.java.levelp.level2.lesson9;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        try {
            MathParser parser = new MathParser("(-(-sin(/n2^2)*2)+  2)/0. 48+(14/n*(12-1 1 ))");
            System.out.print(parser.parse());
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            System.out.println("Check your expression again! It may contains double dots in numbers or a negative number in a fractional degree");
        }

    }
}