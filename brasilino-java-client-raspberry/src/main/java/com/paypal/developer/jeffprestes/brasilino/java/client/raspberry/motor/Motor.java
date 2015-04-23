/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paypal.developer.jeffprestes.brasilino.java.client.raspberry.motor;

import com.paypal.developer.jeffprestes.brasilino.java.client.raspberry.motorcontrollerboard.MotorController;
import com.pi4j.wiringpi.SoftPwm;


/**
 * Class that represents car's motor
 * @author jprestes
 */
public class Motor {
    
    MotorController mControl = null;

    /**
     * Creates a motor that controls car's moviments.
     * 
     * @param motorController contains information about what motor
     * controller board model is being in use. Also, it defines which pins on 
     * Raspberry Pi being used to activates the two motors and for what directions
     * they must turn.
     */
    public Motor(MotorController motorController)  {
        super();
        mControl = motorController;
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
        this.trasDesativa();
        if (mControl.hasPWM())  {
            SoftPwm.softPwmWrite(mControl.getPinPWMPropulsao(), 99);
        }
        mControl.getPinFrente().high();
        System.out.println("frenteAtiva");
    }
    
    /**
     * Activate engine to move backward
     */
    public void trasAtiva()     {
        this.frenteDesativa();
        if (mControl.hasPWM())      {
            SoftPwm.softPwmWrite(mControl.getPinPWMPropulsao(), 99);
        }
        mControl.getPinTras().high();
        System.out.println("trasAtiva");
    }
    
    /**
     * Activate engine to move to right
     */
    public void direitaAtiva()  {
        this.esquerdaDesativa();
        if (mControl.hasPWM())  {
            SoftPwm.softPwmWrite(mControl.getPinPWMDirecao(), 99);
        }
        mControl.getPinDireita().high();
        System.out.println("direitaAtiva");
    }
    
    
    /**
     * Activate engine to move to left
     */
    public void esquerdaAtiva()     {
        this.direitaDesativa();
        if (mControl.hasPWM())  {
            SoftPwm.softPwmWrite(mControl.getPinPWMDirecao(), 99);
        }
        mControl.getPinEsquerda().high();
        System.out.println("esquerdaAtiva");
    }
    
    /** 
     * Deactivate engine that move forwards
     */
    public void frenteDesativa()    {
        if (mControl.hasPWM())  {
            SoftPwm.softPwmWrite(mControl.getPinPWMPropulsao(), 0);
        }
        mControl.getPinFrente().low();
        System.out.println("frenteDesativa");
    }
    
    
    /**
     * Deactivate engine that move backwards
     */
    public void trasDesativa()      {
        if (mControl.hasPWM())  {
            SoftPwm.softPwmWrite(mControl.getPinPWMPropulsao(), 0);
        }
        mControl.getPinTras().low();
        System.out.println("trasDesativa");
    }
    
    
    /**
     * Deactivate engine that moves toward to right
     */
    public void direitaDesativa()   {
        if (mControl.hasPWM())      {
            SoftPwm.softPwmWrite(mControl.getPinPWMDirecao(), 0);
        }
        mControl.getPinDireita().low();
        System.out.println("direitaDesativa");
    }
    
    /**
     * Deactivate engine that moves toward to left
     */
    public void esquerdaDesativa()  {
        if (mControl.hasPWM())      {
            SoftPwm.softPwmWrite(mControl.getPinPWMDirecao(), 0);
        }
        mControl.getPinEsquerda().low();
        System.out.println("esquerdaDesativa");
    }
    
    /**
     * Breaks the car
     */
    public void freia()     {
        this.frenteDesativa();
        this.trasDesativa();
        System.out.println("freia");
    }
    
    /**
     * Test if the motor controllers are OK
     * @return true if yes and no if some error happen
     */
    public boolean isOK()   {
        try {
            this.frenteAtiva();
            this.frenteDesativa();
            this.trasAtiva();
            this.trasDesativa();
            this.esquerdaAtiva();
            this.esquerdaDesativa();
            this.direitaAtiva();
            this.direitaDesativa();
        } catch (Exception ex)  {
            System.err.println("An error occurred when the motors were activated");
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }
}
