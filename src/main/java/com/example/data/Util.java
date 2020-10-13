/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.data;

//import com.mycompany.data.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import static java.util.stream.Collectors.toMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.stream.Stream;

import static com.example.data.Settings.*;
import java.nio.file.NoSuchFileException;
import java.util.Optional;
import java.util.function.Function;

/**
 *
 * @author Admin
 */
public class Util {
    
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
        
//        loadAllJobs().map(j -> {
//            j.setDistance(calcDistance(currUser.getAddressCoord(), j.getAddressCoord()));
//            return j;})
//                .filter(f -> f.getJobType().equalsIgnoreCase(currUser.getSkill()))
//                .sorted(Comparator.comparing(j -> j.getDistance()))
//                .forEach(System.out::println);

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
                            .map(new Function<String, Job>() {
                @Override
                public Job apply(String p) {
                    return new Job(p);
                }
            });

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
    public static boolean saveJobs(String textString) throws IOException{

        //try {
             // Get comments
             Path file = new File(JOBS_FILENAME).toPath();
             Stream<String> comments = Files.lines(file)    // this is here to preserve initial comments
                     .takeWhile(f -> f.startsWith("#"))
                     .collect(toList())
                     .stream();
             //comments.peek(System.out::println);
//    FileOutputStream fos= new FileOutputStream(JOBS_FILENAME);         
//    FileLock fl = fos.getChannel().tryLock();
//    if(fl != null) {
//      System.out.println("Locked File");
//      //Thread.sleep(100);
//      //TimeUnit.SECONDS.sleep(10);
//      TimeUnit.NANOSECONDS.sleep(1);
//      fl.release();
//      System.out.println("Released Lock");
//    }
//    fos.close();

        // Pause to cause probs but only if you're Michael
        
        //TimeUnit.NANOSECONDS.sleep(0);
             
             PrintWriter pw = new PrintWriter(JOBS_FILENAME);
             
             Stream<String> summat =  Arrays.stream(textString.split("\n"));
             
             Stream.concat(comments, summat)
                    //.peek(l -> System.out.println("here: " + l))
                .forEach(l -> pw.println(l));
                              pw.close();
                              //pw.close();
                              
            //System.out.println(" Wrote out jobs.");
                 
//        } catch (NoSuchFileException ex) {//TODO make consistent
//            //System.out.println("Problem saving job: " + ex);
//            throw new NoSuchFileException("Cannot write to file");
////        } catch (InterruptedException ex) {
////            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return true;
    }
        
    // TODO deal with Exceptions - also must test
    // Throw them so can be detected in JobContext?
    
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
    
    private static int[] getIntArray(String coords) {
        
        //int xcoord = Integer.parseInt(coords.split(",")[0]);
        //int ycoord = Integer.parseInt(coords.split(",")[1]);
        int[] intCoords = { Integer.parseInt(coords.split(",")[0]),Integer.parseInt(coords.split(",")[1]) };
        //int[] intCoords = new int[2];
        //intCoords[0] = xcoord;
        //intCoords[1] = ycoord;
        return intCoords;
    }
    
    private static float calcDistance(String startPoint, String endPoint) {
        
        int[] start = getIntArray(startPoint);
        int[] end = getIntArray(endPoint);
        
        float dist = (float) Math.sqrt(Math.pow(Math.abs(start[1] - end[1]),2) + Math.pow(Math.abs(start[0] - end[0]),2));
        
        return  dist;
        
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
                .orElseThrow(); // It's been snatched by someone else!
        
        System.out.println("Job " + address + " is available for " + user.getName());
        
        job.setWorkerPhone(user.getPhoneNumber());    // modify the job
        
//        try {
//            // Cause problem by delaying if michael BEFORE actually saving
//            if (user.getName().equalsIgnoreCase("michael")) {
//                TimeUnit.SECONDS.sleep(5);
//            }
//        } catch (InterruptedException ex) {
//            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
//        }
                    
        Util.saveJob(job);  // try to save modified job
        
        return job;
    }
    
    // Indicates if user has job currently allocated
    // 
    public static Optional<Job> getUserStatus(User user)  throws IOException{
        Optional<Job> job =
                loadAllJobs()
                .filter(f -> (f.getWorkerPhone().equals(user.getPhoneNumber())))
                .filter(f -> f.getState()==0) // Job not completed
                .findAny();
                //.orElseThrow();
        return job;
    }
    
    // Returns ordered job list (order based on order in file
    // i.e. based on when added to file
    public static Job[] getJobList(User user) throws IOException{
        //Map<String,Job> jobList = new LinkedHashMap<>();
        Job[] jobList = loadAllJobs()
//                .map(j -> {
//            j.setDistance(calcDistance(user.getAddressCoord(), j.getAddressCoord()));
//            return j;})
                .filter(f -> f.getJobType().equalsIgnoreCase(user.getSkill()))
                .filter(f -> f.getWorkerPhone().equals(""))   // available to grab
                // Sorted could be passed in TODO
                //.sorted(Comparator.comparing(j -> calcDistance(user.getAddressCoord(), j.getAddressCoord())))
                //.forEach(System.out::println);
                .toArray(Job[]::new);
                      
//             }           
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


        
               
//        String textOutput =
//                currJobs
//                .map(Job::getAddress)
//                .collect(joining("\n"));
//        System.out.println(textOutput);
        
//        State currState = new State();
//        for (int i = 0; i < 10; i++) {
//            currState.incState();
//            System.out.println(currState.getState());
//        }

        //String currentLocation = "1,4";
        //int[] currCoord = getIntArray(currentLocation);