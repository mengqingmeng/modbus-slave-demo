package com.example.modbusslavedemo.modbus4j;

import com.serotonin.modbus4j.ProcessImageListener;

/**
 * @author MQM
 * @description
 * @date 2020-04-24 15:49
 * @copyright TNIT
 * @since 1.0
 */
public class BasicProcessImageListener implements ProcessImageListener {
    @Override
    public void coilWrite(int offset, boolean oldValue, boolean newValue) {
        System.out.println("Coil at " + offset + " was set from " + oldValue + " to " + newValue);
    }

    @Override
    public void holdingRegisterWrite(int offset, short oldValue, short newValue) {
        System.out.println("HoldRrgister at " + offset + " was set from " + oldValue + " to " + newValue);
    }

}
