/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.data;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.NoSuchElementException;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import static com.example.data.Settings.*;
import java.util.Optional;


/**
 *
 * @author Admin
 */
public class Util {
    
    // TODO delete before finalizing. Used only for testing Util methods.
    public static void main(String[] args)  throws IOException{
        
//        String JOBS_FILENAME = Settings.JOBS_FILENAME;
//        String USERS_FILENAME = Settings.USERS_FILENAME;
//        String testFilename = Settings.testFilename;
        //List<Job> currJobs = loadAllJobs(JOBS_FILENAME).collect(toList());
        
        //User currUser = getUser(USERS_FILENAME, "4154842892");
        User currUser = getUser("4155312935");
        
        System.out.println("Current user: " + currUser);
        System.out.println("");
//        
//        System.out.println(currUser.getState().getStateValue());
//        currUser.getState().incStateValue();
//        System.out.println(currUser.getState().getStateValue());
//        System.out.println("");
        
        Job currJob = getJobIfAvailable("1177 Tour Street, Notown", currUser);   // Need full list for saving
        //Job nextJob = getNextJob(JOBS_FILENAME, currJob);
        
        
        currJob.setWorkerPhone("5888");
        currJob.setState(2);
        
        System.out.println("Current job: " + currJob);
        
        //(List<Job> allJobs, String jobAddress, String userPhone, int state, String value)
        
        saveJob(currJob);
        
        System.out.println("=========== Jobs in order ===============");

    }
    
    public static Stream<Job> loadAllJobs() throws IOException {
        return loadAllJobs(JOBS_FILENAME);
    }
    
    public static Stream<Job> loadAllJobs(String filename) throws IOException {
     
        Stream<Job> jobStream = null;
        try {
            Path file = new File(filename).toPath();

            Stream<String> lineDetail = Files.lines(file);
            jobStream
                    = lineDetail
                            .filter(l -> !l.startsWith("#")) // drop comments
                            .filter(l -> l.length() != 0)    // drop empty line
                            .map((String p) -> new Job(p));

        } catch (IOException ioe) {
            //System.out.println("Couldn't open the file:\n " + ioe);
            throw new IOException("Problem opening jobs data file");
        }
        return jobStream;

    }
    
    //public static synchronized void saveJob (Job currJob) {
    public static void saveJob (Job currJob) throws IOException{

        String textString = loadAllJobs()
                //.map(m -> m.getAddress().equals(jobAddress)?currJob:m;});
                .map((Job m) -> {
                    if (m.getAddress().equals(currJob.getAddress())) {
                        //m.setWorkerPhone(userPhone);
                        return currJob;
                    }
                    return m;
                })
                .map(m -> m.toString())
                .collect(joining("\n"));
        
        saveJobs(textString);
    }
    
    //public static synchronized boolean saveJobs(String textString) {
    public static void saveJobs(String textString) throws IOException{

             // get the initial comments
             Path file = new File(JOBS_FILENAME).toPath();
             Stream<String> comments = Files.lines(file)
                     .takeWhile(f -> f.startsWith("#"))
                     .collect(toList())
                     .stream();

             
             PrintWriter pw = new PrintWriter(JOBS_FILENAME);
             
             Stream<String> summat =  Arrays.stream(textString.split("\n"));
             
             // Combine Stream of comments and Stream of Jobs 
             Stream.concat(comments, summat)
                    //.peek(l -> System.out.println("here: " + l))
                .forEach(l -> pw.println(l));
                              pw.close();
                              //pw.close();

    }

    
    public static User getUser(String userPhone) throws IOException, NoSuchElementException {
        return getUser(userPhone, USERS_FILENAME);   
    }
    
    // TODO Consider assembling a list of users first, then trying to find particular user?
    // I.e same technique as used for loading jobs
    public static User getUser(String userPhone, String filename) throws IOException, NoSuchElementException {
        
        String person = null;
        try {
            Path file = new File(filename).toPath();

            Stream<String> lineDetail = Files.lines(file);
            person = lineDetail
                            .filter(l -> l.contains(userPhone))  
                            .findAny()
                            .orElseThrow(); // throws NoSuchElementException
                    
        } catch (NoSuchElementException e) {
            //System.out.println("No such user " + e);
            throw new NoSuchElementException("No such user found");
        } catch (IOException ex) { 
            //System.out.println("Problem opening user data file.\n" + ex);
            throw new IOException("Problem opening user data file");
        }
        return new User(person);
    }
    

    
    // Throws NoSuchElementException if this job has been 
    // claimed by someone else.
    // 
    public static synchronized Job getJobIfAvailable(String address, User user) throws IOException{
    //public static Job getJobIfAvailable(String address, User user) {
        Job job =
                loadAllJobs()
                .filter(f -> f.getWorkerPhone().equals(""))
                .filter(f -> f.getAddress().equals(address))
                .findAny()
                .orElseThrow(); // (NoSuchElementException) It's been grabbed by someone else!
        
        System.out.println("Job " + address + " is available for " + user.getName());
        
        job.setWorkerPhone(user.getPhoneNumber());    // modify the job to claim it for user  
        Util.saveJob(job);  // try to save modified job
        
        return job;
    }
    
    // Gets user's currently allocated job as an Optional
    // No Job then empty Optional.
    public static Optional<Job> getUserStatus(User user)  throws IOException{
        Optional<Job> job =
                loadAllJobs()
                .filter(f -> (f.getWorkerPhone().equals(user.getPhoneNumber())))
                .filter(f -> f.getState()==Settings.AVAILABLE) // Job not completed or marked as problem
                .findAny();
        return job;
    }
    
    // Returns ordered job list (order based on order in file)
    // i.e. based on when added to file
    // TODO maybe use ArrayList not Array?
    public static Job[] getJobList(User user) throws IOException{

        Job[] jobList = loadAllJobs()

                .filter(f -> f.getJobType().equalsIgnoreCase(user.getSkill()))
                .filter(f -> f.getWorkerPhone().equals(""))   // available to grab
                .toArray(Job[]::new);
        
        return jobList;
    }
    
    
    public static String wrapLongLines (String str, int maxLength, int nextLineIndent) {
        StringBuilder output = new StringBuilder();
        String[] lines = str.split("\n");
        for (String line: lines) {
            if (line.length() <= maxLength) {
                output.append(line).append("\n");
            }   
            else {
                String[] words = line.split(" ");
                StringBuilder inneroutput = new StringBuilder();
                inneroutput.append(" ".repeat(nextLineIndent));     // Adding this line removes "hanging indent style"
                for (String word: words) {
                    if (inneroutput.length() + word.length() <= maxLength) {
                        inneroutput.append(word);
                        inneroutput.append(" ");
                    } else {
                        output.append(inneroutput).append("\n");
                        inneroutput = new StringBuilder(" ".repeat(nextLineIndent) + word).append(" ");
                    }
                    //output.append(inneroutput).append("\n");
                }
                output.append(inneroutput).append("\n");
            }

        }
        return output.toString();
    }    
}
