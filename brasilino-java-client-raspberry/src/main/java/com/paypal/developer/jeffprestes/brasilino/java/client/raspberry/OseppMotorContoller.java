/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paypal.developer.jeffprestes.brasilino.java.client.raspberry;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.SoftPwm;

/**
 * Represents my Osepp Motor Controller board Pin Mapping suggested to RaspberryPi
 * http://osepp.com/products/sensors-arduino-compatible/motor-driver/
 * @author jprestes
 */
public class OseppMotorContoller extends MotorController {
    
    private GpioPinDigitalOutput pinEnableDirecao = null;
    private int pinPWMPropulsao = 0;
    
    public OseppMotorContoller()    {
        super();
        this.setHasPWM(true);
        this.setPinPWMPropulsao(1);
        this.setPinPWMDirecao(RaspiPin.GPIO_06);
        this.setPinDireita(RaspiPin.GPIO_02);
        this.setPinEsquerda(RaspiPin.GPIO_03);
        this.setPinFrente(RaspiPin.GPIO_05);
        this.setPinTras(RaspiPin.GPIO_04);
    }
    
    /**
     * @return the pinPWMPropulsao
     */
    public int getPinPWMPropulsao() {
        return pinPWMPropulsao;
    }

    /**
     * @return the pinPWMDirecao
     */
    public GpioPinDigitalOutput getPinPWMDirecao() {
        return pinEnableDirecao;
    }
    
    /**
     * @param pinPWMPropulsao the pinPWMPropulsao to set
     */
    protected void setPinPWMPropulsao(int pinPWMPropulsao) {
        this.pinPWMPropulsao = pinPWMPropulsao;
        SoftPwm.softPwmCreate(this.getPinPWMPropulsao(), 0, 100);
    }

    /**
     * @param pinPWMDirecao the pinPWMDirecao to set
     */
    protected void setPinPWMDirecao(Pin pino) {
        this.pinEnableDirecao = this.getGpioController().provisionDigitalOutputPin(pino, "Motor", PinState.LOW);
    }
}
