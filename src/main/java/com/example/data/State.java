/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.data;

/**
 *
 * @author Admin
 */
public class State {
    
    /*
    0 - not started
    1 - chosen job
    2 - working on job
    3 - completed job or filed a problem
    */
    
    
    
    int state = 0;
    
    int getStateValue(){
        return state;
    }
    
    void incStateValue() {
        state = state == 3?0:state+1; 
    }
    
}
