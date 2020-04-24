package com.example.modbusslavedemo.modbus4j;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusSlaveSet;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.ip.tcp.TcpSlave;

/**
 * @author MQM
 * @description
 * @date 2020-04-24 15:46
 * @copyright TNIT
 * @since 1.0
 */
public class SlaveTest {
    public static void main(String[] args) {
        //创建modbus工厂
        ModbusFactory modbusFactory = new ModbusFactory();
        IpParameters parameters = new IpParameters();

        //创建TCP服务端
        TcpSlave tcpSlave = new TcpSlave(8888,false);
//        final ModbusSlaveSet salve = modbusFactory.createTcpSlave(true);

        //向过程影像区添加数据
        tcpSlave.addProcessImage(Register.getModscanProcessImage(1));

        //获取寄存器
        tcpSlave.getProcessImage(1);

        try {
            tcpSlave.start();
        } catch (ModbusInitException e) {
            e.printStackTrace();
        }
    }
}
