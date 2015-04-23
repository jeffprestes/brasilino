/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paypal.developer.jeffprestes.brasilino.java.client.raspberry.communication;

import com.paypal.developer.jeffprestes.brasilino.java.client.raspberry.motor.Motor;
import com.paypal.developer.jeffprestes.brasilino.java.client.raspberry.motorcontrollerboard.OseppMotorContoller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jprestes
 */
public class SocketComm  {

    BufferedReader leitorLinhas;
    InputStreamReader leitorCaracteres;
    InputStream leitorSocket;
    private Socket socketComm;
    private int port = 8282;
    private ServerSocket server = null;
    private Motor m = null;
    
    /**
     * Create new Socket Listener to receives moviments commands from controller.
     * @param motor The motor controller that will be used to controls the car.
     */
    public SocketComm(Motor motor)  {
        motor = motor;
    }
    
    public void socketStart()    {
        
        try {
            
            System.out.println("Starting socket on port 8282");
            server = new ServerSocket(port);
            System.out.println("Server listening socket on port 8282");
            
            String cmd = "";
            
            while (true)    {
                Socket s = server.accept();
                System.out.println("Socket created.");
                leitorSocket = s.getInputStream();
                leitorCaracteres = new InputStreamReader(leitorSocket);
                leitorLinhas = new BufferedReader(leitorCaracteres);
            
            
                cmd = leitorLinhas.readLine();
                System.out.println(cmd);
                if (cmd != null)    {
                    cmd = cmd.substring(0, cmd.length()-1);
                    
                    m.opere(cmd);
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(SocketComm.class.getName()).log(Level.SEVERE, "Error during socket initialization...", ex);
        }
        
    }
    

    /**
     * @return the socketComm
     */
    public Socket getSocketComm() {
        return socketComm;
    }

    /**
     * @param socketComm the socketComm to set
     */
    public void setSocketComm(Socket socketComm) {
        this.socketComm = socketComm;
    }

    
    public void close()     {
        try {
            server.close();
            this.getSocketComm().close();
        } catch (IOException ex) {
            Logger.getLogger(SocketComm.class.getName()).log(Level.SEVERE, "Error: socket server could not be closed", ex);
        }
    }
    
}
