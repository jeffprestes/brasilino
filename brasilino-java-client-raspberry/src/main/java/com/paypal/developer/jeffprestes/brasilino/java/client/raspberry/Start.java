/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paypal.developer.jeffprestes.brasilino.java.client.raspberry;

import com.paypal.developer.jeffprestes.brasilino.java.client.raspberry.communication.SocketComm;
import com.paypal.developer.jeffprestes.brasilino.java.client.raspberry.motor.Motor;
import com.paypal.developer.jeffprestes.brasilino.java.client.raspberry.motorcontrollerboard.OseppMotorContoller;
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
        System.out.println("Activating motor...");
        Motor m = new Motor(new OseppMotorContoller());
        if (m.isOK())   {
            System.out.println("Motor activated...");
            SocketComm socketComm = null;
            socketComm = new SocketComm(m);
            socketComm.socketStart();
        }
    }
    
}
