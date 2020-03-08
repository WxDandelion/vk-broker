package com.zy.broker.VKGPS.codec;

import com.zy.broker.VKGPS.PackageData;

import java.util.Arrays;

public class MsgEncoder {
    public byte[] encodeToRespMsg(PackageData req)
            throws Exception {

        String response = "*MGY" + req.getMsgType().getTypeCode() + req.getMsgType().getTypeKeyword() + "#";
        return response.getBytes("ascii");
    }
}
