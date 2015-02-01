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
        SocketComm socketComm = null;
        Thread socketThread = null;
        
        if (serialComm.connect())   {
            socketComm = new SocketComm(serialComm);
            
            if (socketComm.socketStart())   {
                socketThread = new Thread(socketComm);
                socketThread.start();
                System.out.println("Communications are set. Waiting for orders...");
            }   else    {
                System.err.println("Socket could not be started. Check error above.");
            }
        }   else    {
            System.err.println("Serial communication could not be estabelished. Check errors above.");
        }
        
        
    }
    
}
