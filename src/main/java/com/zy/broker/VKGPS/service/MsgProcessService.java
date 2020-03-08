package com.zy.broker.VKGPS.service;

import com.alibaba.fastjson.JSON;
import com.zy.broker.VKGPS.PackageData;
import com.zy.broker.VKGPS.codec.MsgEncoder;
import com.zy.broker.utils.DevSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgProcessService extends BaseMsgProcessService{
    private final Logger log = LoggerFactory.getLogger(getClass());
    private MsgEncoder msgEncoder;
    private DevSessionManager sessionManager;

    public MsgProcessService() {
        this.msgEncoder = new MsgEncoder();
        this.sessionManager = DevSessionManager.getInstance();
    }

    public void processBaseMsg(PackageData req) throws Exception {
        byte[] bs = this.msgEncoder.encodeToRespMsg(req);
        super.send2Client(req.getChannel(), bs);
    }
}
