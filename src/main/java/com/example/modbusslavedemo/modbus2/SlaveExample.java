package com.example.modbusslavedemo.modbus2;

import com.digitalpetri.modbus.FunctionCode;
import com.digitalpetri.modbus.requests.*;
import com.digitalpetri.modbus.responses.*;
import com.digitalpetri.modbus.slave.ModbusTcpSlave;
import com.digitalpetri.modbus.slave.ModbusTcpSlaveConfig;
import com.digitalpetri.modbus.slave.ServiceRequestHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
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

    HashMap<Integer,Object> map = new HashMap<>();


    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ModbusTcpSlaveConfig config = new ModbusTcpSlaveConfig.Builder().build();
    private final ModbusTcpSlave slave = new ModbusTcpSlave(config);

    public SlaveExample() {
        map.put(0, 'S');
        map.put(2, 1);
        map.put(4, 3.56f);
    }

    public void start() throws ExecutionException, InterruptedException {
        slave.setRequestHandler(new ServiceRequestHandler() {
            // 0x03
            @Override
            public void onReadHoldingRegisters(ServiceRequest<ReadHoldingRegistersRequest, ReadHoldingRegistersResponse> service) {
//                String clientRemoteAddress = service.getChannel().remoteAddress().toString();
//                String clientIp = clientRemoteAddress.replaceAll(".*/(.*):.*", "$1");
//                String clientPort = clientRemoteAddress.replaceAll(".*:(.*)", "$1");
                ReadHoldingRegistersRequest request = service.getRequest();

                ByteBuf registers = PooledByteBufAllocator.DEFAULT.buffer(request.getQuantity());
                //logger.error("**function:"+request.getFunctionCode().getCode()+",address:"+request.getAddress()+  ",quantity:" + request.getQuantity());
                for (Object value : map.values()) {
                    if (value instanceof Character){
                        registers.writeInt(97);
                    }else if (value instanceof Integer){
                        registers.writeInt((Integer)value);
                    }else if (value instanceof Float){
                        registers.writeFloat((Float) value);
                    }
                }

                service.sendResponse(new ReadHoldingRegistersResponse(registers));

                ReferenceCountUtil.release(request);
            }

            @Override
            public void onWriteSingleRegister(ServiceRequest<WriteSingleRegisterRequest, WriteSingleRegisterResponse> service) {
                WriteSingleRegisterRequest request = service.getRequest();
                FunctionCode functionCode = request.getFunctionCode();
                int address = request.getAddress();
                int value = request.getValue();
                logger.error("**客户端来值:"+functionCode+",address:"+address+  ",value:" + value);

                service.sendResponse(new WriteSingleRegisterResponse(address,value));

                ReferenceCountUtil.release(request);
            }

            @Override
            public void onWriteMultipleRegisters(ServiceRequest<WriteMultipleRegistersRequest, WriteMultipleRegistersResponse> service) {
                short slaveId= service.getUnitId();
                logger.info("**onWriteMultipleRegisters**:slaveId:" +slaveId);
                WriteMultipleRegistersRequest request = service.getRequest();
                int address = request.getAddress();
                FunctionCode functionCode = request.getFunctionCode();
                int quantity = request.getQuantity();
                ByteBuf byteBuf = request.getValues();
                int readableBytes = byteBuf.readableBytes();

                byte[] bytes = new byte[readableBytes];
                byteBuf.readBytes(bytes);
                Object value = map.get(address);
                if (value instanceof Character){
                    System.out.println("result: " +   value);
                    map.put(address,value);
                }else if (value instanceof Integer){
                    map.put(address,bytesToInt(bytes));
                    System.out.println("result: " +   bytesToInt(bytes));
                }else if (value instanceof Float){
                    map.put(address,bytesToInt(bytes));
                    System.out.println("result: " +   bytesToInt(bytes));
                }
                service.sendResponse(new WriteMultipleRegistersResponse(address,quantity));
                System.out.println("write bytes length:" + bytes.length + ",code:"+functionCode.getCode()+",address:" + address+",value:" + byte2float(bytes,0));

            }

            @Override
            public void onWriteSingleCoil(ServiceRequest<WriteSingleCoilRequest, WriteSingleCoilResponse> service) {
                WriteSingleCoilRequest request = service.getRequest();
                FunctionCode functionCode = request.getFunctionCode();
                int address = request.getAddress();
                int value = request.getValue();
                logger.error("**function:"+functionCode+",address:"+address+  ",value:" + value);
            }

            @Override
            public void onReadInputRegisters(ServiceRequest<ReadInputRegistersRequest, ReadInputRegistersResponse> service) {
                ReadInputRegistersRequest request = service.getRequest();
                FunctionCode functionCode = request.getFunctionCode();
                int address = request.getAddress();
                int quantity = request.getQuantity();
                ByteBuf registers = PooledByteBufAllocator.DEFAULT.buffer(request.getQuantity());

                for (int i = 0; i < quantity; i++) {
                    registers.writeShort(0);
                }

                service.sendResponse(new ReadInputRegistersResponse(registers));

                ReferenceCountUtil.release(request);
                logger.error("**function:"+functionCode+",address:"+address+  ",quantity:" + quantity);
            }

            @Override
            public void onReadCoils(ServiceRequest<ReadCoilsRequest, ReadCoilsResponse> service) {
                System.out.println("onReadInputRegisters");
            }

            @Override
            public void onReadDiscreteInputs(ServiceRequest<ReadDiscreteInputsRequest, ReadDiscreteInputsResponse> service) {
                System.out.println("onReadDiscreteInputs");
            }
        });

        slave.bind("192.168.0.109", 8888).get();
    }

    public void stop() {
        slave.shutdown();
    }

    public static int bytesToInt(byte[] bs) {
        int a = 0;
        for (int i = bs.length - 1; i >= 0; i--) {
            a += bs[i] * Math.pow(255, bs.length - i - 1);
        }
        return a;
    }

    public static float byte2float(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }
}
