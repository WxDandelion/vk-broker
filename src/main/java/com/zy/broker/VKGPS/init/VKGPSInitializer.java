package com.zy.broker.VKGPS.init;

import com.zy.broker.VKGPS.handler.VKGPSServerHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

public class VKGPSInitializer extends ChannelInitializer<Channel> {

    private final VKGPSServerHandler vkServerHandler = new VKGPSServerHandler();

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
                //每次收到消息，会打印ip和消息字段，以16进制显示
                .addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("*MG20".getBytes()),
                        Unpooled.copiedBuffer("#".getBytes())))
                //自定义协议解析handler
                .addLast(vkServerHandler);

    }
}
