/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paypal.developer.jeffprestes.brasilino.java.client.raspberry;

import com.pi4j.io.gpio.RaspiPin;

/**
 * Represents my Osepp Motor Controller board Pin Mapping suggested to RaspberryPi
 * http://osepp.com/products/sensors-arduino-compatible/motor-driver/
 * @author jprestes
 */
public class OseppMotorContoller extends MotorController {
    
    public OseppMotorContoller()    {
        super();
        this.setHasPWM(true);
        this.setPinPWMDirecao(23);
        this.setPinPWMPropulsao(1);
        this.setPinDireita(RaspiPin.GPIO_02);
        this.setPinEsquerda(RaspiPin.GPIO_03);
        this.setPinFrente(RaspiPin.GPIO_05);
        this.setPinTras(RaspiPin.GPIO_04);
    }
    
}
