/*
 * This one waits for a message and responds as below
 *
 */
package com.example.handler;

//import com.twilio.twiml.MessagingResponse;
//import com.mycompany.handler.*;
//import com.notused.Sequence5;
//import com.mycompany.test.WebPage;
//import com.twilio.twiml.MessagingResponse;
//import com.twilio.twiml.messaging.Body;
//import com.twilio.twiml.messaging.Message;
//import javax.servlet.http.HttpSession;
import com.example.test.WebPage;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.NoSuchElementException;
import spark.Session;
//import com.twilio.twiml.Body;
//import com.twilio.twiml.Message;

import static spark.Spark.*;    // So that "get" and "post" can be used in code

public class SMSWebhookHandler
{
    public static void main(String[] args) {

        post("/jobs", (req, res) -> {
            String message = req.queryParams("Body");
            String phoneNumFrom = req.queryParams("From");
            
            String msgToUser;   // return message to user
            
            System.out.println("Body param: " + message);

            Session session = req.session();

            JobContext currSequence = session.attribute("context");
            System.out.println("Trying to get JobContext for ... " + phoneNumFrom);
            
            // TODO possibly 2 try/catch blocks? One for creating context, one for running each step?
            // (Just for clarity in coding)
            try {
                if (currSequence == null) {
                    currSequence = new JobContext(phoneNumFrom);
                    System.out.println("...had to create a new one.");
                }

                msgToUser = currSequence.runStep(message);
            } catch (IOException | NoSuchElementException ex) {
                System.out.println("Problem: " + ex);
                msgToUser = "There's a problem with the system. Try again later.";
                return msgToUser;
            } 

            session.attribute("context", currSequence);
            System.out.println("JobContext saved to session");
                        
            //return TwiMLUtil.getResponseString(msgToUser); // Response enclosed in TwiML
            return msgToUser;  // Response without XML for testing

        });

    }
}
