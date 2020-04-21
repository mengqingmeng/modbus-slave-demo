package com.example.modbusslavedemo.modbus2;

import com.digitalpetri.modbus.requests.ReadHoldingRegistersRequest;
import com.digitalpetri.modbus.requests.WriteSingleCoilRequest;
import com.digitalpetri.modbus.requests.WriteSingleRegisterRequest;
import com.digitalpetri.modbus.responses.ReadHoldingRegistersResponse;
import com.digitalpetri.modbus.responses.WriteSingleCoilResponse;
import com.digitalpetri.modbus.responses.WriteSingleRegisterResponse;
import com.digitalpetri.modbus.slave.ModbusTcpSlave;
import com.digitalpetri.modbus.slave.ModbusTcpSlaveConfig;
import com.digitalpetri.modbus.slave.ServiceRequestHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * @author MQM
 * @description
 * @date 2020-04-21 11:15
 * @copyright TNIT
 * @since 1.0
 */
public class SlaveExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new SlaveExample().start();
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ModbusTcpSlaveConfig config = new ModbusTcpSlaveConfig.Builder().build();
    private final ModbusTcpSlave slave = new ModbusTcpSlave(config);

    public SlaveExample() {
    }

    public void start() throws ExecutionException, InterruptedException {
        slave.setRequestHandler(new ServiceRequestHandler() {
            @Override
            public void onReadHoldingRegisters(ServiceRequest<ReadHoldingRegistersRequest, ReadHoldingRegistersResponse> service) {
//                String clientRemoteAddress = service.getChannel().remoteAddress().toString();
//                String clientIp = clientRemoteAddress.replaceAll(".*/(.*):.*", "$1");
//                String clientPort = clientRemoteAddress.replaceAll(".*:(.*)", "$1");
                logger.info("read holding register");
                ReadHoldingRegistersRequest request = service.getRequest();

                ByteBuf registers = PooledByteBufAllocator.DEFAULT.buffer(request.getQuantity());

                for (int i = 0; i < request.getQuantity(); i++) {
                    registers.writeShort(i);
                }

                service.sendResponse(new ReadHoldingRegistersResponse(registers));

                ReferenceCountUtil.release(request);
            }

            @Override
            public void onWriteSingleRegister(ServiceRequest<WriteSingleRegisterRequest, WriteSingleRegisterResponse> service) {
                logger.info(service.toString());
            }

            @Override
            public void onWriteSingleCoil(ServiceRequest<WriteSingleCoilRequest, WriteSingleCoilResponse> service) {
                logger.info(service.toString() );
            }
        });

        slave.bind("192.168.0.119", 8888).get();
    }

    public void stop() {
        slave.shutdown();
    }
}