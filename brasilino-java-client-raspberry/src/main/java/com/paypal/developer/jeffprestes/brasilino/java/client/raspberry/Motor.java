/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paypal.developer.jeffprestes.brasilino.java.client.raspberry;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;


/**
 * Class that represents car's motor
 * @author jprestes
 */
public class Motor {
    
    final GpioController gpio = GpioFactory.getInstance();
    final GpioPinDigitalOutput pinFrente = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "Motor", PinState.LOW);
    final GpioPinDigitalOutput pinTras = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "Motor", PinState.LOW);
    final GpioPinDigitalOutput pinDireita = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Motor", PinState.LOW);
    final GpioPinDigitalOutput pinEsquerda = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "Motor", PinState.LOW);

    public Motor()  {
        super();
        this.frenteAtiva();
        this.frenteDesativa();
        this.trasAtiva();
        this.trasDesativa();
        this.esquerdaAtiva();
        this.esquerdaDesativa();
        this.direitaAtiva();
        this.direitaDesativa();
    }
    
    /**
     * Sends commands to internal motors
     * @param cmd 
     * FA, FD = forward activate/deactivate 
     * EA, ED = left activate/deactivate 
     * DA, DD = right activate/deactivate 
     * TA, TD = backward activate/deactivate 
     * PP stops
     */
    public void opere(String cmd)   {
        switch (cmd)    {
                    
            case "EA":
                this.esquerdaAtiva();
                break;

            case "ED":
                this.esquerdaDesativa();
                break;

            case "DA":
                this.direitaAtiva();
                break;

            case "DD":
                this.direitaDesativa();
                break;

            case "FA":
                this.frenteAtiva();
                break;

            case "FD":
                this.frenteDesativa();
                break;

            case "TA":
                this.trasAtiva();
                break;

            case "TD":
                this.trasDesativa();
                break;

            case "PP":
                this.freia();
                break;
        }
    }
    
    /**
     * Activate engine to move forward
     */
    public void frenteAtiva()    {
        this.frenteDesativa();
        this.pinFrente.high();
        System.out.println("frenteAtiva");
    }
    
    /**
     * Activate engine to move backward
     */
    public void trasAtiva()     {
        this.trasDesativa();
        this.pinTras.high();
        System.out.println("trasAtiva");
    }
    
    /**
     * Activate engine to move to right
     */
    public void direitaAtiva()  {
        this.direitaDesativa();
        this.pinDireita.high();
        System.out.println("direitaAtiva");
    }
    
    
    /**
     * Activate engine to move to left
     */
    public void esquerdaAtiva()     {
        this.esquerdaDesativa();
        this.pinEsquerda.high();
        System.out.println("esquerdaAtiva");
    }
    
    /** 
     * Deactivate engine that move forwards
     */
    public void frenteDesativa()    {
        this.pinFrente.low();
        System.out.println("frenteDesativa");
    }
    
    
    /**
     * Deactivate engine that move backwards
     */
    public void trasDesativa()      {
        this.pinTras.low();
        System.out.println("trasDesativa");
    }
    
    
    /**
     * Deactivate engine that moves toward to right
     */
    public void direitaDesativa()   {
        this.pinDireita.low();
        System.out.println("direitaDesativa");
    }
    
    /**
     * Deactivate engine that moves toward to left
     */
    public void esquerdaDesativa()  {
        this.pinEsquerda.low();
        System.out.println("esquerdaDesativa");
    }
    
    /**
     * Breaks the car
     */
    public void freia()     {
        this.pinFrente.low();
        this.pinTras.low();
        System.out.println("freia");
    }
}
