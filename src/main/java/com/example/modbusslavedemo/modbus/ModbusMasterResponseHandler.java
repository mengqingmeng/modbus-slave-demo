package com.example.modbusslavedemo.modbus;


import com.github.zengfr.easymodbus4j.func.AbstractRequest;
import com.github.zengfr.easymodbus4j.handler.ModbusResponseHandler;
import com.github.zengfr.easymodbus4j.logging.ChannelLogger;
import com.github.zengfr.easymodbus4j.processor.ModbusMasterResponseProcessor;
import com.github.zengfr.easymodbus4j.protocol.ModbusFunction;
import com.github.zengfr.easymodbus4j.protocol.tcp.ModbusFrame;
import com.github.zengfr.easymodbus4j.util.ModbusFrameUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author MQM
 * @description
 * @date 2020-04-20 18:19
 * @copyright TNIT
 * @since 1.0
 */
@ChannelHandler.Sharable
public class ModbusMasterResponseHandler extends ModbusResponseHandler {

    private static final ChannelLogger logger = ChannelLogger.getLogger(ModbusMasterResponseHandler.class);
    private ModbusMasterResponseProcessor processor;

    public short getTransactionIdentifierOffset() {
        return this.processor.getTransactionIdentifierOffset();
    }
    public ModbusMasterResponseHandler(ModbusMasterResponseProcessor processor) {
        super(true);
        this.processor = processor;
    }

    @Override
    protected boolean processResponseFrame(Channel channel, ModbusFrame frame) {
        if (this.processor.isShowFrameDetail()) {
            ModbusFrameUtil.showFrameLog(logger, channel, frame, true);
        }
        return super.processResponseFrame(channel, frame);
    }

    @Override
    protected int getReqTransactionIdByRespTransactionId(int respTransactionIdentifierOffset) {
        return respTransactionIdentifierOffset - this.getTransactionIdentifierOffset();
    }

    protected int getRespTransactionIdByReqTransactionId(int reqTransactionIdentifier) {
        return reqTransactionIdentifier + this.getTransactionIdentifierOffset();
    }

    @Override
    public ModbusFrame getResponseCache(int reqTransactionIdentifier,short funcCode) throws Exception {
        int respTransactionIdentifier = getRespTransactionIdByReqTransactionId(reqTransactionIdentifier);
        return super.getResponseCache(respTransactionIdentifier,  funcCode);
    }

    @Override
    protected boolean processResponseFrame(Channel channel, int unitId, AbstractRequest reqFunc, ModbusFunction respFunc) {
        return this.processor.processResponseFrame(channel, unitId, reqFunc, respFunc);

    }

}
