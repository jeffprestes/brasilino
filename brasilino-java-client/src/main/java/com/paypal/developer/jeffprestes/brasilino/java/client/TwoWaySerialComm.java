/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paypal.developer.jeffprestes.brasilino.java.client;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class TwoWaySerialComm {
    
    private static final String PORT_NAMES[] = {
        "/dev/tty.usbmodem1451", //MacOS
        "/dev/ttyUSB0", // Linux
        "/dev/ttyACM0", //RaspberryPi
        "COM3", // Windows
    };
    
    private OutputStream serialWriter = null;
    private Thread readerThread = null;
    private SerialPort serialPort = null;
    
    /**
     * Connect to Serial Device listed down below:
     * /dev/tty.usbmodem1411 - MacOS
     * /dev/ttyUSB0 - Linux
     * /dev/ttyACM0 - RaspberryPi
     * COM3 - Windows
     * @return true in case of successful communication
     */
    public boolean connect( )  {
        
        boolean retorno = false;
    
        System.out.println("Initialing...");
        System.out.println("Scanning ports...");
        
        CommPortIdentifier portIdentifier = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        // iterate through, looking for the port
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portNameTest : PORT_NAMES) {
                if (currPortId.getName().equals(portNameTest)) {
                    portIdentifier = currPortId;
                    System.out.println("Port found: " + portIdentifier.getName());
                    break;
                }
            }
        }
        
        if( portIdentifier.isCurrentlyOwned() ) {
            System.out.println( "Error: Port is currently in use" );
        
        } else {
            int timeout = 2000;
            CommPort commPort;
            
            try {
                commPort = portIdentifier.open( this.getClass().getName(), timeout );
 
                if( commPort instanceof SerialPort ) {
                    serialPort = ( SerialPort )commPort;
                    serialPort.setSerialPortParams( 9600,
                                            SerialPort.DATABITS_8,
                                            SerialPort.STOPBITS_1,
                                            SerialPort.PARITY_NONE );

                    InputStream in = serialPort.getInputStream();
                    serialWriter = serialPort.getOutputStream();

                    readerThread = new Thread( new SerialReader( in ) );
                    readerThread.start();

                    retorno = true;
                    
                } else {
                    System.out.println( "Error: Only serial ports are handled by this software." );
                }
                    
            } catch (PortInUseException ex) {
                System.err.println("Error: The port selected is already in use.");
                Logger.getLogger(TwoWaySerialComm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedCommOperationException ex) {
                System.err.println("Error: This kind of communication is not supported by this O.S.");
                Logger.getLogger(TwoWaySerialComm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                System.err.println("Error: Could not send or receive data from serial device");
                Logger.getLogger(TwoWaySerialComm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return retorno;
    }
 
    /**
     * Writes data to Serial Device
     * @param dados Information to be sent
     * @return true in case of successful communication
     */
    public boolean write(String dados)     {
        boolean retorno = false;
        
        if (this.serialWriter!=null)    {
            
            byte[] bDados = dados.getBytes();
            try {
                this.serialWriter.write(bDados);
                retorno = true;
            }   catch (IOException ex)  {
                System.err.println("Error: couldn't send data to serial device: " + ex.getLocalizedMessage());
                ex.printStackTrace();
            }
        }
            
        
        return retorno;
    }
    
    
    public void close()  {
        readerThread.interrupt();
        serialPort.close();
    }
    
    public static class SerialReader implements Runnable {
 
        InputStream in;
 
        public SerialReader( InputStream in ) {
            this.in = in;
        }
 
        public void run() {
            byte[] buffer = new byte[ 1024 ];
            int len = -1;
            try {
                while( ( len = this.in.read( buffer ) ) > -1 ) {
                    System.out.print( new String( buffer, 0, len ) );
                }
            } catch( IOException e ) {
                e.printStackTrace();
            }
        }
    }
 
}