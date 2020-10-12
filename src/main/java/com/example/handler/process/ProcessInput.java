/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.handler.process;

//import com.example.handler.process.*;
import com.example.data.Util;
import com.example.handler.JobContext;
import java.io.IOException;

/**
 *
 * @author Admin
 */

// Should this contain the text from last one?

public interface ProcessInput {
    
    String runStep(JobContext context, String inputMessage) throws IOException;
    
    // Saves the job list to the context, returns appropriate user message
    // and sets the state appropriately
    default String createJobList(JobContext context) throws IOException{
        String msgToUser = "";
        context.setJobList(Util.getJobList(context.getUser())); // Reload job list
        if (context.getJobList().length != 0) {
            for (int i = 0; i < context.getJobList().length; i++) {
            msgToUser += (i + 1) + " - " 
                    + context.getJobList()[i].getName() + ": " 
                    + context.getJobList()[i].getJobType() + "\n"
                    + Util.wrapLongLines(context.getJobList()[i].getAddress(), 30, 4); // using i + 1 so job list not zero-based
            context.setCurrState(new ProcessInputChoose());
            }
             msgToUser += "\nText the number of the job you'd like to tackle.";
        } else {
             msgToUser += "There are no jobs available now."
               + "Try texting 'list' later";
           context.setCurrState(new ProcessInputInitial());
        }

        return msgToUser;
    }
    
}
