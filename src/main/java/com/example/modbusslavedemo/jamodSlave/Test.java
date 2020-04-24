package com.example.modbusslavedemo.jamodSlave;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.net.ModbusTCPListener;
import net.wimpi.modbus.procimg.*;

/**
 * @author MQM
 * @description
 * @date 2020-04-24 11:20
 * @copyright TNIT
 * @since 1.0
 */
public class Test {
    public static void main(String[] args) {
        ModbusTCPListener listener = null;
        SimpleProcessImage spi = null;
//        int port = Modbus.DEFAULT_PORT;
        int port = 8888;
        //1. Set port number from commandline parameter
        if(args != null && args.length ==1) {
            port = Integer.parseInt(args[0]);
        }

        //2. Prepare a process image
        spi = new SimpleProcessImage();
        spi.addDigitalOut(new SimpleDigitalOut(true));
        spi.addDigitalOut(new SimpleDigitalOut(false));
        spi.addDigitalIn(new SimpleDigitalIn(false));
        spi.addDigitalIn(new SimpleDigitalIn(true));
        spi.addDigitalIn(new SimpleDigitalIn(false));
        spi.addDigitalIn(new SimpleDigitalIn(true));
        spi.addRegister(new SimpleRegister(251));
        spi.addInputRegister(new SimpleInputRegister(0));

        //3. Set the image on the coupler
        ModbusCoupler.getReference().setProcessImage(spi);
        ModbusCoupler.getReference().setMaster(false);
        ModbusCoupler.getReference().setUnitID(1);
        //4. Create a listener with 3 threads in pool
        listener = new ModbusTCPListener(3);
        listener.setPort(port);
        listener.start();

    }
}
