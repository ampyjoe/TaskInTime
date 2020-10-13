/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.handler.process;

import com.example.data.Util;
import com.example.handler.JobContext;
import java.io.IOException;
import java.util.Optional;

/**
 *
 * @author Admin
 */


// Should never get here unless user has a job assigned. If no job assigned, will throw NoSuchElementException

public class ProcessInputProgress implements ProcessInput {
    
    @Override
    public String runStep(JobContext context, String message)  throws IOException{
            String msgToUser = "";
            
            if ( message.equalsIgnoreCase("c")) {

                msgToUser = processValidMessage(context, 2, "Excellent! Text 'list' to see joblist again.");
                
            } else if ( message.equalsIgnoreCase("p")) {
                msgToUser = processValidMessage(context, 2, "Oh dear - we'll look into it!\nText 'list' to see joblist again.");

            } else msgToUser += "Sorry, you need to text C or P";
            
        return msgToUser;
    }
    
    // TODO Could be moved to default method on Interface if used elsewhere
    private String processValidMessage(JobContext context, int state, String message)  throws IOException{
        context.getCurrJob().get().setState(state);
        Util.saveJob(context.getCurrJob().get());
        context.setCurrState(new ProcessInputInitial());
        context.setCurrJob(Optional.empty());
        return message;
    }
}
