package com.example.modbusslavedemo.modbus4j;

import com.serotonin.modbus4j.BasicProcessImage;
import com.serotonin.modbus4j.code.DataType;
import com.sun.org.apache.xpath.internal.operations.String;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * @author MQM
 * @description
 * @date 2020-04-24 15:48
 * @copyright TNIT
 * @since 1.0
 */
public class Register {
    static BasicProcessImage getModscanProcessImage(int slaveId) {
        BasicProcessImage processImage = new BasicProcessImage(slaveId);
        processImage.setInvalidAddressValue(Short.MIN_VALUE);

        //创建 保持寄存器
        short[] register1 = new short[2];
        processImage.setHoldingRegister(0,register1);

        processImage.addListener(new BasicProcessImageListener());
        return processImage;
    }

    public static byte[] getBytes(char[] chars) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }


}
