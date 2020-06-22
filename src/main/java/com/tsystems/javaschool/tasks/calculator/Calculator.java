package com.tsystems.javaschool.tasks.calculator;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Calculator {
    //calc
    public String evaluate(String statement) {
        double sum = 0.0; // total sum of arithmetic calculation

       if (!checkStatement(statement))
            return null;

        if (statement.contains("("))
            statement = calcParenthesesBlocks(statement);

        if(!checkStatement(statement)) return null;

        //replace all the minuses with +- we can split the whole string by + preserving the mathematical operational signs
        String minuslessStr = statement.replaceAll("-", "+-");

        //replace all minuses except the minuses which are preceded * or succeeded by multiplication or division
        minuslessStr = correctMisplacedPluses(minuslessStr, statement);

        //split by +
        String[] byPluses = minuslessStr.split("\\+");

        for (int i = 0; i < byPluses.length; i++) {
            // check on digits
            if(isValidDigit(byPluses[i])) {
                if(byPluses[i].equals("")) continue; //if string contains whitespace, we will not add it
                sum = sum  + Double.parseDouble(byPluses[i]);
            }
            // for string contains *
            if(byPluses[i].contains("*")) sum += getMultiplyResult(byPluses[i]);

            // for string contains /
            if(byPluses[i].contains("/") && !byPluses[i].contains("*"))
                sum += getDivisionResult(byPluses[i]);

        }
                return roundDigit(sum);
    }

    // check the validity of the string
    private boolean checkStatement(String statement) {
        int openParth = 0;
        int closeParth = 0;

        if (statement == null || statement.length() == 0 ) return false;

        if ((statement.indexOf("..") != -1) ||
                (statement.indexOf("++") != -1)||
                (statement.indexOf("--") != -1)||
                (statement.indexOf("**") != -1)||
                (statement.indexOf("//") != -1)) return false;
        if (statement.indexOf('(') > statement.indexOf(')')) return false; //check on ")" before "("
        if(isDividedByZero(statement))
            return false;
        for(int i = 0; i < statement.length(); i++) {
            char ch = statement.charAt(i);
            if( ch == '(') openParth++;
            if(ch == ')') closeParth++;
            if(Character.isAlphabetic(ch)) return false;
            if(!Character.isDigit(ch) && ch != '+' && ch != '-' &&  ch != '/' &&  ch != '*' &&  ch != '.'
                    && ch != '(' && ch != ')')  return false;
        }
        if(openParth != closeParth) return false;
    return true;
    }

    //method to calc parentheses block
    private String calcParenthesesBlocks(String statement) {
        int parentheses  = counterOfParenthesesBlocks(statement);
        while (parentheses > 0) {
            int lastOpenedParentheses = statement.lastIndexOf('('); // last opened parenthesese
            int closeParenthesesOfLastOpened = statement.indexOf(')', lastOpenedParentheses);

            // close parenthesese of last opened parentheses
            String innerBlockOfParentheses = statement.substring(lastOpenedParentheses, closeParenthesesOfLastOpened +1);

            // get inner block
            String result = calcSingleParenthesesBlock(innerBlockOfParentheses);

            if (result == null)
                statement = statement.replace(innerBlockOfParentheses, "0");
            else
                statement = statement.replace(innerBlockOfParentheses, result);
            parentheses  = counterOfParenthesesBlocks(statement);
        }
        return statement;
    }

    // for calc ()
    private String calcSingleParenthesesBlock(String statement) {
        String result = "";
        int startInd = statement.indexOf("(");
        int endInd = statement.indexOf(")", startInd + 1);
        String subStr = statement.substring(startInd + 1 , endInd);
        result = evaluate(subStr);
        return result;
    }

    // count of ()
    private int counterOfParenthesesBlocks(String statement) {
        int count = 0;
        for (int i = 0; i < statement.length(); i++)
            if (statement.charAt(i) == ')') count++;

        return count;
    }

    // do rounding to 4 significant digits
    private static String roundDigit(double digit) {
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);


        return df.format(digit).replaceAll("," , ".");
    }
    // for *
    private double getMultiplyResult(String string) {
        double result = 1.0;

        //split the string
        String[] byMultiplies = string.split("\\*");

        for(String multiply : byMultiplies) {
            if(multiply.contains("/")) result *=  getDivisionResult(multiply); //compute strings with */ separately
            else result *= Double.parseDouble(multiply);
        }

        return result;
    }
    // for /
    private double getDivisionResult(String string) {
        String[] byDivision = string.split("\\/");

        //get the first x
        double x = Double.parseDouble(byDivision[0]); ;

        //we will iterate byDivision size - 1 times,
        //so that in the iteration, we can use i+1 index
        for(int i = 0; i < byDivision.length - 1; i++)
            x /= Double.parseDouble(byDivision[i + 1]);
        return x;
    }

    //check if a string contains only valid digits
    private static boolean isValidDigit(String string) {
        if(string.contains("/") || string.contains("*")) return false;
        return true;
    }

    // fix the minus operand when it is adjacent to * or / on both sides
    private String correctMisplacedPluses(String noMinuses, String statement) {
        StringBuilder retString = new StringBuilder(noMinuses);
        for(int i = 0; i < statement.length(); i++) {
            if(statement.indexOf('-', i) != -1) {
                int ind = statement.indexOf('-', i);
                if(ind > 0 &&((statement.charAt(ind-1) == '*' ||statement.charAt(ind-1) == '/')  ||
                        (statement.charAt(ind+1) == '*' ||statement.charAt(ind +1) == '/'))) {
                    retString.delete(ind, ind+1);
                    i = i + ind;
                }
            }
        }

        return retString.toString();
    }

    // check divident is divided by zero
    private boolean isDividedByZero(String str) {
        int ind = str.indexOf('/');
        //if after / sign, the divisor is zero, return true
        if(ind < str.length() && str.charAt(ind + 1) == '0') return true;
        String leftString = "";
        //get the next index after '/' index
        ind = ind + 1;
        char ch = str.charAt(ind);
        while(Character.isDigit(ch) && ind < str.length() - 1) {
            leftString += ch;
            ind = ind + 1;
            if(ind < str.length() - 1)
                ch = str.charAt(ind);
        }
        int minusIndex = ind;
        if(str.charAt(minusIndex) == '-' && ind < str.length()) {
            //get the substring between - and immediat next operand
            String rightSubString = "";
            minusIndex += 1;
            ch = str.charAt(minusIndex);
            while(Character.isDigit(ch) && minusIndex < str.length()) {
                rightSubString += ch;
                minusIndex = minusIndex + 1;

                if(minusIndex < str.length() - 1)
                    ch = str.charAt(minusIndex);
            }
            return leftString.equals(rightSubString);
        }
        return false;
    }
}




