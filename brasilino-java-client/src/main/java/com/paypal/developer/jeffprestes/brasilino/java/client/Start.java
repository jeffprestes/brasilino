/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paypal.developer.jeffprestes.brasilino.java.client;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jprestes
 */
public class Start {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        TwoWaySerialComm serialComm = new TwoWaySerialComm();
        if (serialComm.connect())   {
            serialComm.write("AZ");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
            }
            serialComm.write("AZ");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
            }
            serialComm.write("FZ");
        }
        
        
    }
    
}
