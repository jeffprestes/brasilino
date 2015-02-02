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
 * 
 * @author jprestes
 */
public class Motor {
    
    final GpioController gpio = GpioFactory.getInstance();
    final GpioPinDigitalOutput pinFrente = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_, "Motor", PinState.LOW);
    final GpioPinDigitalOutput pinTras = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "Motor", PinState.LOW);
    final GpioPinDigitalOutput pinDireita = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "Motor", PinState.LOW);
    final GpioPinDigitalOutput pinEsquerda = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "Motor", PinState.LOW);

            
    public Motor()  {
        
    }
}
