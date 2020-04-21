package com.example.modbusslavedemo;

import com.example.modbusslavedemo.modbus.ExampleModbusMasterResponseProcessor;
import com.example.modbusslavedemo.modbus.ExampleModbusSlaveRequestProcessor;
import com.example.modbusslavedemo.modbus.ModbusMasterResponseHandler;
import com.example.modbusslavedemo.modbus.ModbusSlaveRequestHandler;
import com.github.zengfr.easymodbus4j.processor.ModbusMasterResponseProcessor;
import com.github.zengfr.easymodbus4j.server.ModbusServer;
import com.github.zengfr.easymodbus4j.server.ModbusServerRtuFactory;
import com.github.zengfr.easymodbus4j.server.ModbusServerTcpFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ModbusSlaveDemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ModbusSlaveDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
