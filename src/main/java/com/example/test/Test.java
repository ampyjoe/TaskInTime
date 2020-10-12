/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.test;

import com.example.data.Job;
import com.example.data.Util;
import java.io.File;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
//import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import java.util.stream.Stream;

/**
 *
 * @author Admin
 */
public class Test {
    
    // TODO set it to cleanup after completion, not before
    
    // Allow passing in params file with default use default params file
    // Or pass in reset just to reset the jobsdata.txt file.
    
    // Note:
    // context NOT stored in memory so will only work properly within a single call.
    
    // If jobsdata filename not passed in, then replace data in jobsdata.txt each
    // run. Otherwise don't, so can test subsequent calls (i.e. new session) 
    // as datastore changes.
    
    
    // TODO will it be able to save the cookie
    //static Map<String,HttpClient> theMap = new HashMap<>();
    static Map<String,ClientWrapper> theMap = new HashMap<>();
    static int howMany;
    static String userDetailsFile = "userdataTEST.txt"; // Maybe put in main (so can be cmdline parameter)
    
    public static void main(String[] args) throws InterruptedException, IOException {
        

        
        String filename = "webhookTestParams_1.txt";
        String url = "http://localhost:4567/jobs";
        
        if (args.length == 1) filename = args[0];
        
        //Map<String,HttpClient> theMap;
        
        getParamList2(filename, url);   // Do the test
        
        
        // Cleanup
                String jobDetails =
                Util.loadAllJobs("jobsdata_bkup.txt")  // backup jobs details
                .collect(mapping(Job::toString,joining("\n")));
        
        try {
            Util.saveJobs(jobDetails);
        } catch (IOException io) {
            System.out.println("Error in Test.java: " + io);
        }
        
        //System.exit(0);
        
    }
    
    public static void getParamList2 (String filename, String url) throws InterruptedException {
         try {
            Path file = new File(filename).toPath();

                 //List<String> instructionList = Files.lines(file)
                        // .collect(toList());
                 
//                 for (String l: instructionList) {
//                     theMap.putIfAbsent(l.split("\\^")[0].split(",")[1], getNewClient());
//                     
//                     sendCommand(l.split("\\^")[0].split(",")[1], 
//                                    url,
//                                    l.split("\\^")[1].split(",")[1]);
//                 }

// This approach prob preferred rather than for loop above? Doesn't need to make a List first. 
// But catching Exceptions with Streams always a bit clumsy. Can we throw from lambda block?
// 

                Files.lines(file)
                        .filter(p -> !p.startsWith("#"))    // ignore comments
                        .forEach(l -> {
                            if (!theMap.containsKey(l.split("\\^")[0].split(",")[1]))   // so getNewClient not called unnecessarily
                             theMap.put(l.split("\\^")[0].split(",")[1], getNewClient());
                             
                        try {
                            sendCommand(l.split("\\^")[0].split(",")[1], 
                                    url,
                                    l.split("\\^")[1].split(",")[1]);
                        } catch (IOException | InterruptedException ex) {
                            //Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                            System.out.println("Error in Test - can't get user file");
                        }
                             
                 });
        
         } catch (IOException ex) {
                    //Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error in Test - can't get test details file");
         }
    }
    
//    public static void getParamList (String filename, String url) {
//                
//        try {
//            Path file = new File(filename).toPath();
//               //Map<String, HttpClient> 
//               theMap =
//                    Files.lines(file)
//                        .collect(toMap(k -> k.split("\\^")[0].split(",")[1],
//                         v -> getNewClient()
//                        ,(s, a) -> a));
//                        theMap.forEach((a, b) -> System.out.println(a + " | " + b.toString()));
//
//                       // one more time now that we know the distinct users
//                       // TODO - poss distinct a better approach??
//                       Files.lines(file)
//                               .forEach(l -> {
//                try {
//                    sendCommand(l.split("\\^")[0].split(",")[1], 
//                            url, 
//                            l.split("\\^")[1].split(",")[1]);
//                } catch (IOException ex) {
//                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            });
//
//                     
//        } catch (IOException ex) {
//            System.out.println("Prob: " + ex);;
//        }
//
//    }
    
    static ClientWrapper getNewClient() {
        
        howMany++;
        
        HttpClient theClient =  HttpClient.newBuilder()
                  .cookieHandler(new CookieManager())
                  .build();
        
        ClientWrapper clientWrapper = new ClientWrapper();
        clientWrapper.theClient = theClient;
        clientWrapper.thisClientIndex = howMany;
        
        return clientWrapper;
    }

    private static void sendCommand(String c, String url, String s) 
            throws IOException, InterruptedException {
        
        //System.out.println(c + ":" + s);
        var client = theMap.get(c);
        //System.out.println(">>> " + theMap.get(c).thisClientIndex);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            var request = HttpRequest.newBuilder(
                URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")    // yep, needed cos POST!
                .POST(BodyPublishers.ofString("Body=" + s + "&From=" + c))
                
           .build();

        System.out.println("=".repeat(35).indent(1 + (client.thisClientIndex - 1) * 40));
        //System.out.println("User request: " + s);
        
        // TODO prob should specify file for users here for testing purposes
        
        System.out.println((Util.getUser(c, userDetailsFile).getName() + ":" + s).indent(1 + (client.thisClientIndex - 1) * 40));
        //String output = Util.wrapLongLines(HttpResponse.BodyHandlers.ofString().body(), 35);
        String response = client.theClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        System.out.println(Util.wrapLongLines(response, 30, 0).indent(5 + (client.thisClientIndex - 1) * 40));
        //System.out.println(client.send(request, output));

        //String indent = "/t";
        //indent = indent.repeat(client.)
        
        //
//        //System.out.println("User request: " + s);
//        System.out.println(s + ":" + client.send(request, HttpResponse.BodyHandlers.ofString()).body());
        //System.out.println("=".repeat(35));

        
        //TimeUnit.SECONDS.sleep(30);
    
    }
    
    
}

class ClientWrapper {   // TODO encapsulate or make it a member of Test
    HttpClient theClient;
    int thisClientIndex;
}
