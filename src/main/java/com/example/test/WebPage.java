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
public class WebPage {
    
    // TODO phoneNumFrom prob not needed
    public static String getReturnString(String systemReply, String message, String phoneNumFrom) {
        
        
        String buildPage = "";
        
        buildPage += ("<HTML>\n");

        buildPage += ("<form id='params'  method='get' action='jobs'> "
                + "Message: <input type='text' name='Body'/><br>"
                + "From: <input type='text' name='From'/><br>"
                + "<input type='submit' />"
                + "</form>"
                );
        
        buildPage += ("<b>Employee message is: " + message + "</b><p>\n");
        buildPage += ("<b>System message is: " + systemReply + "</b><p>\n");
        
        buildPage += ("</HTML>\n");
        
        return buildPage;
    }
    
}
