package com.paypal.developer.jeffprestes.brasilino.java.client.raspberry.motorcontrollerboard;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.wiringpi.SoftPwm;

/**
 * Represents Motor Controller GPIO Mapper
 * @author jprestes
 */
abstract public class MotorController {
    
    private GpioController gpio = null;
    private boolean hasPWM = false;
    private int pinPWMPropulsao = 0;
    private int pinPWMDirecao = 0;
    private GpioPinDigitalOutput pinFrente = null;
    private GpioPinDigitalOutput pinTras = null;
    private GpioPinDigitalOutput pinDireita = null;
    private GpioPinDigitalOutput pinEsquerda = null;
    
    public MotorController()    {
        gpio = GpioFactory.getInstance();
    }

    /**
     * @return the hasPWM
     */
    public boolean hasPWM() {
        return hasPWM;
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
    public int getPinPWMDirecao() {
        return pinPWMDirecao;
    }

    /**
     * @return the pinFrente
     */
    public GpioPinDigitalOutput getPinFrente() {
        return pinFrente;
    }

    /**
     * @return the pinTras
     */
    public GpioPinDigitalOutput getPinTras() {
        return pinTras;
    }

    /**
     * @return the pinDireita
     */
    public GpioPinDigitalOutput getPinDireita() {
        return pinDireita;
    }

    /**
     * @return the pinEsquerda
     */
    public GpioPinDigitalOutput getPinEsquerda() {
        return pinEsquerda;
    }

    /**
     * @param hasPWM the hasPWM to set
     */
    protected void setHasPWM(boolean hasPWM) {
        this.hasPWM = hasPWM;
        if (this.hasPWM())  {
            Gpio.wiringPiSetup();
        }
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
    protected void setPinPWMDirecao(int pinPWMDirecao) {
        this.pinPWMDirecao = pinPWMDirecao;
        SoftPwm.softPwmCreate(this.getPinPWMDirecao(), 0, 100);
    }

    /**
     * @param pinFrente the pinFrente to set
     */
    protected void setPinFrente(Pin pino) {
        this.pinFrente = gpio.provisionDigitalOutputPin(pino, "Motor", PinState.LOW);
    }

    /**
     * @param pinTras the pinTras to set
     */
    protected void setPinTras(Pin pino) {
        this.pinTras = gpio.provisionDigitalOutputPin(pino, "Motor", PinState.LOW);
    }

    /**
     * @param pinDireita the pinDireita to set
     */
    protected void setPinDireita(Pin pino) {
        this.pinDireita = gpio.provisionDigitalOutputPin(pino, "Motor", PinState.LOW);
    }

    /**
     * @param pinEsquerda the pinEsquerda to set
     */
    protected void setPinEsquerda(Pin pino) {
        this.pinEsquerda = gpio.provisionDigitalOutputPin(pino, "Motor", PinState.LOW);
    }
    
    
}
