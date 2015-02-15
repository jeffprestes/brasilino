/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paypal.developer.jeffprestes.brasilino.java.client;

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
        
        if (serialComm.connect())   {
            socketComm = new SocketComm(serialComm);
            socketComm.socketStart();
        }   else    {
            System.err.println("Serial communication could not be estabelished. Check errors above.");
        }
        
        
    }
    
}
