/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.test;

import com.example.data.Job;
import com.example.data.Util;
import java.io.IOException;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;

/**
 *
 * @author Admin
 */
public class ResetData {
    
    public static void main(String[] args)  throws IOException{
        
                        String jobDetails =
                Util.loadAllJobs("jobsdata_bkup.txt")  // backup jobs details
                .collect(mapping(Job::toString,joining("\n")));
        
        Util.saveJobs(jobDetails);
        
    }
    
}
