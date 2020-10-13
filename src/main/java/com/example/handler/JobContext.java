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
    
    private ProcessInput currState;  // 
    private Job[] jobList;           // Required as jobList user is choosing from needed in ProcessInputChooseJob
    private Optional<Job> currJob = null;
    private User user = null;
    
    // If session expired needs to check if currently allocated a job. If so, state is Progress
    public JobContext(String phoneNum) throws IOException, NoSuchElementException{ 
        this.user = Util.getUser(phoneNum);
        System.out.println("User details: "  + user);

        currJob = Util.getUserStatus(user);
        if (currJob.isPresent()) {
            this.currState = new ProcessInputProgress();
        }   else this.currState = new ProcessInputInitial();
    }
    
    
    public String runStep(String message) throws IOException {
        
         return currState.runStep(this, message);
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
    
}
