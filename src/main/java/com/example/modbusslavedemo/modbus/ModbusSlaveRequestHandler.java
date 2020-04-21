package com.example.modbusslavedemo.modbus;

import com.github.zengfr.easymodbus4j.func.request.*;
import com.github.zengfr.easymodbus4j.func.response.*;
import com.github.zengfr.easymodbus4j.handler.ModbusRequestHandler;
import com.github.zengfr.easymodbus4j.logging.ChannelLogger;
import com.github.zengfr.easymodbus4j.processor.ModbusSlaveRequestProcessor;
import com.github.zengfr.easymodbus4j.protocol.ModbusFunction;
import com.github.zengfr.easymodbus4j.protocol.tcp.ModbusFrame;
import com.github.zengfr.easymodbus4j.util.ModbusFrameUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

/**
 * @author MQM
 * @description
 * @date 2020-04-20 18:29
 * @copyright TNIT
 * @since 1.0
 */
@ChannelHandler.Sharable
public class ModbusSlaveRequestHandler extends ModbusRequestHandler {
    private static final ChannelLogger logger = ChannelLogger.getLogger(ModbusSlaveRequestHandler.class);

    private ModbusSlaveRequestProcessor processor;

    public short getTransactionIdentifierOffset() {
        return this.processor.getTransactionIdentifierOffset();
    }

    public ModbusSlaveRequestHandler(ModbusSlaveRequestProcessor processor) {

        this.processor = processor;
    }

    @Override
    protected int getRespTransactionIdByReqTransactionId(int reqTransactionIdentifier) {
        return reqTransactionIdentifier + this.getTransactionIdentifierOffset();
    }

    @Override
    protected ModbusFunction processRequestFrame(Channel channel, ModbusFrame frame) {
        if (this.processor.isShowFrameDetail()) {
            ModbusFrameUtil.showFrameLog(logger, channel, frame, true);
        }
        return super.processRequestFrame(channel, frame);
    }

    @Override
    protected WriteSingleCoilResponse writeSingleCoil(short unitIdentifier, WriteSingleCoilRequest request) {

        return this.processor.writeSingleCoil(unitIdentifier, request);
    }

    @Override
    protected WriteSingleRegisterResponse writeSingleRegister(short unitIdentifier, WriteSingleRegisterRequest request) {
        return this.processor.writeSingleRegister(unitIdentifier, request);
    }

    @Override
    protected ReadCoilsResponse readCoils(short unitIdentifier, ReadCoilsRequest request) {

        return this.processor.readCoils(unitIdentifier, request);
    }

    @Override
    protected ReadDiscreteInputsResponse readDiscreteInputs(short unitIdentifier, ReadDiscreteInputsRequest request) {

        return this.processor.readDiscreteInputs(unitIdentifier, request);
    }

    @Override
    protected ReadInputRegistersResponse readInputRegisters(short unitIdentifier, ReadInputRegistersRequest request) {

        return this.processor.readInputRegisters(unitIdentifier, request);
    }

    @Override
    protected ReadHoldingRegistersResponse readHoldingRegisters(short unitIdentifier, ReadHoldingRegistersRequest request) {

        return this.processor.readHoldingRegisters(unitIdentifier, request);

    }

    @Override
    protected WriteMultipleCoilsResponse writeMultipleCoils(short unitIdentifier, WriteMultipleCoilsRequest request) {
        return this.processor.writeMultipleCoils(unitIdentifier, request);
    }

    @Override
    protected WriteMultipleRegistersResponse writeMultipleRegisters(short unitIdentifier, WriteMultipleRegistersRequest request) {

        return this.processor.writeMultipleRegisters(unitIdentifier, request);
    }

}
