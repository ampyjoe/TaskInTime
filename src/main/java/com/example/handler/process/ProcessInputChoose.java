/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.handler.process;

import com.example.data.Job;
import com.example.data.Util;
import com.example.handler.JobContext;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 *
 * @author Admin
 */
public class ProcessInputChoose implements ProcessInput {
    
    @Override
    public String runStep(JobContext context, String message)  throws IOException{
        String msgToUser = null;

        try {
            
            int selected = Integer.parseInt(message) - 1;   // - 1 to correct for zero-based
            
            if ( selected < context.getJobList().length) {   

                // If job has already been chosen by another user will throw NoSuchElementException                
                Job job = Util.getJobIfAvailable(context.getJobList()[selected].getAddress(), context.getUser());   // TODO needs to lock file during

                // Save job on context
                context.setCurrJob(Optional.of(job));

                msgToUser += "Contact " + context.getCurrJob().get().getName() + " on " 
                        + context.getCurrJob().get().getJobPhone() + "\n" 
                        + "Text C when job completed or P to indicate a problem.";
                context.setCurrState(new ProcessInputProgress());


            } else {    // Number out of range so remind with list of jobs
                
                msgToUser += "Please text a valid job number.\n\n";
                msgToUser += createJobList(context);

            }
            
            // Job grabbed by someone else
            } catch (NoSuchElementException nsee) {
                    msgToUser += "Sorry - someone has just chosen that job.\n\n";
                    msgToUser += createJobList(context);
                }
            // Invalid input - not a number
            catch (NumberFormatException nfe) {
                msgToUser += "Please text a valid job number.\n\n";
                msgToUser += createJobList(context);
            }
        return msgToUser;
    }
}
