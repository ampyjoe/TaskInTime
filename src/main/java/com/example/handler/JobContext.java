/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.handler;

//import handler.*;
import com.example.handler.process.ProcessInputInitial;
import com.example.handler.process.ProcessInput;
import com.example.data.User;
import com.example.data.Job;
import com.example.data.Util;
import com.example.handler.process.ProcessInputProgress;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Admin
 */

public class JobContext{

    /**
     * @param currState the currState to set
     */
    public void setCurrState(ProcessInput currState) {
        this.currState = currState;
    }
    
    // TODO remove - only used to check 1 sequence per user connecting
    // This is number contexts created since starting SMSWebhookHandler (not during this test)
    private static int howMany; 
    
    private final int contextNumber;
    
    private ProcessInput currState;  // 
    private Job[] jobList;           // Required as jobList user is choosing from needed in ProcessInputChooseJob
    private Optional<Job> currJob = null;             // Make Optional?
    private User user = null;
    
    // Will need to check if working on a job. If so, state is Progress
    // However, after List there should be a timeout, and in that case
    // a number is an error as you need to List again.
    
    // currJob could also be an Optional and if it comes back empty... do the necessary. Better I think.

    public JobContext(String phoneNum) throws IOException, NoSuchElementException{ 
        this.user = Util.getUser(phoneNum);
        System.out.println("User details: "  + user);

        currJob = Util.getUserStatus(user);
        if (currJob.isPresent()) {
            this.currState = new ProcessInputProgress();
        }   else this.currState = new ProcessInputInitial();
        
        contextNumber = howMany++;  
        System.out.println(">>>>>>>>> New context created: " + contextNumber);
  
    }
    
    
    public String runStep(String message) throws IOException {
        
//        try {
            return currState.runStep(this, message);
//        } catch (IOException ioe) {
//            System.out.println("Problem loading file data:\n" + ioe);
//            return "There is a problem. Try again later";
//        }
    }

    /**
     * @return the jobList
     */
    public Job[] getJobList() {
        return jobList;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param jobList the jobList to set
     */
    public void setJobList(Job[] jobList) {
        this.jobList = jobList;
    }

    /**
     * @return the currJob
     */
    public Optional<Job> getCurrJob() {
        return currJob;
    }

    /**
     * @param currJob the currJob to set
     */
    public void setCurrJob(Optional<Job> currJob) {
        this.currJob = currJob;
    }

    /**
     * @return the howMany
     */
    public static int getHowMany() {
        return howMany;
    }
    
}
