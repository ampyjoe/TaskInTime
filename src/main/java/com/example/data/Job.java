/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.data;

import java.util.NoSuchElementException;

/**
 *
 * @author Admin
 */
public class Job {
    
    private final String name;
    private final String address;
    private final String jobType;
    private final String jobPhone;
    private String workerPhone;
    private int state;

    
    public Job (String lineDetail) throws NoSuchElementException {
        
        try {
            String[] params = lineDetail.split("\\^",-2);

            name = params[0];
            address = params[1];
            jobType = params[2];
            workerPhone = params[3];
            jobPhone = params[4];
            state = Integer.valueOf(params[5]);
        } catch (ArrayIndexOutOfBoundsException aioe) {
            throw new NoSuchElementException("Jobs file corrupted");
        }
        
    }
    
    @Override
    public String toString() {
        return getName() + "^" + getAddress() + "^" + getJobType() + "^" 
                + getWorkerPhone() + "^" + getJobPhone() + "^" + getState();
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return the jobType
     */
    public String getJobType() {
        return jobType;
    }


    /**
     * @return the state
     */
    public int getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * @return the jobRecipentPhone
     */
    public String getWorkerPhone() {
        return workerPhone;
    }

    /**
     * @param employeePhone the jobRecipentPhone to set
     */
    public void setWorkerPhone(String employeePhone) {
        this.workerPhone = employeePhone;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the jobPhone
     */
    public String getJobPhone() {
        return jobPhone;
    }

    
}
