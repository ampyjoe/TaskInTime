/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.handler;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;

/**
 *
 * @author Admin
 */
public class TwiMLUtil {
    
    public static String getResponseString(String sysMessage) {
        
            Body body = new Body
                    .Builder(sysMessage)
                    .build();
            Message sms = new Message
                    .Builder()
                    .body(body)
                    .build();
            MessagingResponse twiml = new MessagingResponse
                    .Builder()
                    .message(sms)
                    .build();
            //System.out.println("** " + twiml + ":" + twiml.toXml());
            return twiml.toXml();
    }
    
}
