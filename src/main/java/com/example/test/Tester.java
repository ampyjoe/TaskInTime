/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.test;

/**
 *
 * @author Admin
 */
public class Tester {
    
    public static void main(String[] args) {
        
        String testString = "summat\n"
                + "summat else\n" 
                + "A big long string with loads of text " 
                + "stuff\n"
                + "summat else";
        
         String testString2 = "summat\n"
                + "summat else\n" 
                + "A big long string with loads of text " 
                + "stuff\n"
                + "And another big long string that goes on and on and on. And"
                + " never really gets to what it needs to say!\n"
                + "summat else of mid-length or so.\n"
                + "Aye";
        
        
        System.out.println(testString2);
        
        System.out.println("\n");
        
        System.out.println(wrapLongLines(testString2, 20));
        
        

        
    }
    
    
    public static String shortenStringFullWords(String str, int maxLength) {
        StringBuilder output = new StringBuilder();
        String[] tokens = str.split(" ");
        for (String token: tokens) {
            if (output.length() + token.length() <= maxLength - 3) {
                output.append(token);
                output.append(" ");
            } else {
                return output.toString().trim() + "...";
            }
        }
        return output.toString().trim();
    }
    
    public static String wrapLongLines (String str, int maxLength) {
        StringBuilder output = new StringBuilder();
        String[] lines = str.split("\n");
        for (String line: lines) {
            if (line.length() <= maxLength) {
                output.append(line).append("\n");
            }   
            else {
                String[] words = line.split(" ");
                StringBuilder inneroutput = new StringBuilder();
                for (String word: words) {
                    if (inneroutput.length() + word.length() <= maxLength) {
                        inneroutput.append(word);
                        inneroutput.append(" ");
                    } else {
                        output.append(inneroutput).append("\n");
                        inneroutput = new StringBuilder(word).append(" ");
                    }
                    //output.append(inneroutput).append("\n");
                }
                output.append(inneroutput).append("\n");
            }

        }
        return output.toString();
    }
    
}
