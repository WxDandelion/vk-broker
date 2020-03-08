package com.zy.broker.VKGPS.service;

import com.zy.broker.utils.DevSessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseMsgProcessService {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected DevSessionManager sessionManager;

    public BaseMsgProcessService() {
        this.sessionManager = DevSessionManager.getInstance();
    }

    protected ByteBuf getByteBuf(byte[] arr) {
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(arr.length);
        byteBuf.writeBytes(arr);
        return byteBuf;
    }

    public void send2Client(Channel channel, byte[] arr) throws InterruptedException {
        ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer(arr)).sync();
        if (!future.isSuccess()) {
            log.error("发送数据出错:{}", future.cause());
        }
    }
}
