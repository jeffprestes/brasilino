package com.paypal.developer.jeffprestes.brasilino.java.client.raspberry;

import com.pi4j.io.gpio.RaspiPin;

/**
 * @author marabesi
 */
public class L298NSimple extends MotorController {

    public L298NSimple() {
        super();
        this.setHasPWM(false);
        /*this.setPinPWMDirecao(1);
        this.setPinPWMPropulsao(1);*/
        this.setPinDireita(RaspiPin.GPIO_07);
        this.setPinEsquerda(RaspiPin.GPIO_00);
        this.setPinFrente(RaspiPin.GPIO_02);
        this.setPinTras(RaspiPin.GPIO_03);
    }
}
