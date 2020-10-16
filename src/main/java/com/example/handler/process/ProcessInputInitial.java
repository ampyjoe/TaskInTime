/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.handler.process;

import com.example.handler.JobContext;
import java.io.IOException;

/**
 *
 * @author Admin
 */


public class ProcessInputInitial implements ProcessInput {

    @Override
    public String runStep(JobContext context, String msgFromUser) throws IOException{
        String msgToUser = "";
        if (msgFromUser.equalsIgnoreCase("list")) {
            
            msgToUser += "Welcome " + context.getUser().getName() + "\n\n"; 
            
            // Creates job list, sets state and returns message for user
            msgToUser += createJobList(context);    

        } else {   // Invalid input
            msgToUser += "Sorry, that's not valid input.\n"
                    + "You need to text 'list' to get a list of jobs first";
        }

        return msgToUser;
    }
   
}
