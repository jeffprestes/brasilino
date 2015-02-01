/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paypal.developer.jeffprestes.brasilino.java.client;

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
    private TwoWaySerialComm serialComm;
    private Socket socketComm;
    private int port = 8282;
    private ServerSocket server = null;
    
    
    /**
     * Initialize Socket Communication on port 8282
     * @param serialComm Serial Communication to send commands received from Socket
     */
    public SocketComm (TwoWaySerialComm serialComm)     {
        
        this.setSerialComm(serialComm);
        
    }
    
    public void socketStart()    {
        
        try {
            
            System.out.println("Starting socket on port 8282");
            server = new ServerSocket(port);
            System.out.println("Server listening socket on port 8282");
            
            while (true)    {
                Socket s = server.accept();
                leitorSocket = s.getInputStream();
                leitorCaracteres = new InputStreamReader(leitorSocket);
                leitorLinhas = new BufferedReader(leitorCaracteres);
            
                TwoWaySerialComm serialComm = this.getSerialComm();
            
                serialComm.write(leitorLinhas.readLine());
            }
            
        } catch (IOException ex) {
            Logger.getLogger(SocketComm.class.getName()).log(Level.SEVERE, "Error during socket initialization...", ex);
        }
        
    }
    

    /**
     * @return the serialComm
     */
    public TwoWaySerialComm getSerialComm() {
        return serialComm;
    }

    /**
     * @param serialComm the serialComm to set
     */
    public void setSerialComm(TwoWaySerialComm serialComm) {
        this.serialComm = serialComm;
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
