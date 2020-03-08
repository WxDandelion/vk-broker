package com.zy.broker.VKGPS.handler;

import com.alibaba.fastjson.JSON;
import com.zy.broker.VKGPS.TPMSConsts;
import com.zy.broker.VKGPS.PackageData;
import com.zy.broker.VKGPS.PackageData.ResSetting;
import com.zy.broker.VKGPS.PackageData.MsgType;
import com.zy.broker.VKGPS.codec.MsgDecoder;
import com.zy.broker.VKGPS.service.MsgProcessService;
import com.zy.broker.req.LocationMsg;
import com.zy.broker.req.WarningMsg;
import com.zy.broker.utils.CommonSession;
import com.zy.broker.utils.DevSessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import static io.netty.util.ReferenceCountUtil.release;

@Slf4j
@ChannelHandler.Sharable
public class VKGPSServerHandler extends ChannelInboundHandlerAdapter {

    private MsgProcessService msgProcessService;
    // 会话管理
    private final DevSessionManager devSessionManager;
    private final MsgDecoder decoder;

    public VKGPSServerHandler() {
        this.msgProcessService = new MsgProcessService();
        this.devSessionManager = DevSessionManager.getInstance();
        this.decoder = new MsgDecoder();
    }

    // 对接收到的消息进行处理
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException {
        try {
            ByteBuf buf = (ByteBuf) msg;
            if (buf.readableBytes() <= 0) {
                // ReferenceCountUtil.safeRelease(msg);
                return;
            }

            byte[] bs = new byte[buf.readableBytes()];
            buf.readBytes(bs);

            String ms = new String(bs, "ascii");
            PackageData pkg = this.decoder.String2PackageData(ms);
            pkg.setChannel(ctx.channel());
            this.processPackageData(pkg);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            release(msg);
        }
    }

    /**
     *
     * 处理业务逻辑
     *
     * @param packageData
     *
     */
    private void processPackageData(PackageData packageData) {
        final ResSetting res = packageData.getResSetting();
        final MsgType msg = packageData.getMsgType();
        // 状态类消息
        if (TPMSConsts.msg_upload_status.equals(msg.getTypeCode())) {
            // 1. 终端心跳-消息体为空 ==> 平台通用应答
            if (TPMSConsts.key_heartbeat.equals(msg.getTypeKeyword())) {
                log.info(">>>>>[收到终端心跳],终端ID={}", res.getDevId());
                try {
                    this.msgProcessService.processBaseMsg(packageData);
                    log.info("<<<<<[终端心跳回复],终端ID={}", res.getDevId());
                } catch (Exception e) {
                    log.error("<<<<<[终端心跳]处理错误,终端ID={},err={}", res.getDevId(), e.getMessage());
                    e.printStackTrace();
                }
                // 2. 警情上报 ==> 平台通用应答
            } else if(TPMSConsts.key_warning.equals(msg.getTypeKeyword())) {
                log.info(">>>>>[收到警情上报],终端ID={}", res.getDevId());
                try {
                    WarningMsg warningMsg = this.decoder.toWarningMsg(packageData);
                    log.info("警情信息:" + JSON.toJSONString(warningMsg));
                    // 应答
                    this.msgProcessService.processBaseMsg(packageData);
                    log.info("<<<<<[警情上报回复],终端ID={}", res.getDevId());
                } catch (Exception e) {
                    log.error("<<<<<[警情上报]处理错误,终端ID={},err={}", res.getDevId(), e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        // 定位类
        else if (TPMSConsts.msg_upload_loc.equals(msg.getTypeCode())) {
            // 3. 定位信息汇报 ==> 平台通用应答
            if (TPMSConsts.key_loc.equals(msg.getTypeKeyword())) {
                log.info(">>>>>[收到位置信息],终端ID={}", res.getDevId());
                try {
                    LocationMsg locationMsg = this.decoder.toLocationMsg(packageData);
                    log.info("位置信息:" + JSON.toJSONString(locationMsg));
                    // 应答
                    this.msgProcessService.processBaseMsg(packageData);
                    log.info("<<<<<[位置信息回复],终端ID={}", res.getDevId());
                } catch (Exception e) {
                    log.error("<<<<<[位置信息]处理错误,终端ID={},err={}", res.getDevId(), e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        // 其他情况
        else {
            log.error(">>>>>>[未知消息类型],终端ID={},关键类型={},package={}", res.getDevId(), msg.getTypeCode(),
                    packageData);
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        log.error("发生异常:{}", cause.getMessage());
        cause.printStackTrace();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        CommonSession session = CommonSession.buildSession(ctx.channel());
        devSessionManager.put(session.getId(), session);
        log.info("终端连接:{}", session);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final String sessionId = ctx.channel().id().asLongText();
        CommonSession session = devSessionManager.findBySessionId(sessionId);
        this.devSessionManager.removeBySessionId(sessionId);
        log.info("终端断开连接:{}", session);
        ctx.channel().close();
        // ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                CommonSession session = this.devSessionManager.removeBySessionId(CommonSession.buildId(ctx.channel()));
                log.error("服务器主动断开连接:{}", session);
                ctx.close();
            }
        }
    }

    private void release(Object msg) {
        try {
            ReferenceCountUtil.release(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
