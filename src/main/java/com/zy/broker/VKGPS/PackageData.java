package com.zy.broker.VKGPS;

import com.alibaba.fastjson.annotation.JSONField;
import io.netty.channel.Channel;

import java.util.Arrays;

public class PackageData {
    /**
     * 5byte 消息头
     */
    protected MsgHeader msgHeader;
    /*
     * 回复相关
     */
    protected ResSetting resSetting;
    /*
     * 具体功能关键字及指令
     */
    protected MsgType msgType;

    /**
     * 协议尾 1byte
     */
    protected int checkSum;

    @JSONField(serialize=false)
    protected Channel channel;


    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

    public ResSetting getResSetting() {
        return resSetting;
    }

    public void setResSetting(ResSetting resSetting) {
        this.resSetting = resSetting;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public void setMsgHeader(MsgHeader msgHeader) {
        this.msgHeader = msgHeader;
    }

    public int getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(int checkSum) {
        this.checkSum = checkSum;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "PackageData [msgHeader=" + msgHeader + ", checkSum="
                + checkSum + ", address=" + channel + "]";
    }

    // 大概是没用
    public static class MsgHeader {
        // 协议关键字
        protected String keyword;

        // 标识
        protected String mark;

        // 协议版本标识
        protected int grade;

        public String getKeyword() {
            return keyword;
        }
        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getMark() {
            return mark;
        }
        public void setMark(String keyword) {
            this.mark = mark;
        }

        public int getGrade() {
            return grade;
        }
        public void setGrade(int keyword) {
            this.grade = grade;
        }


    }
    /*
     * 回复相关
     */
    public static class ResSetting {
        // 回复属性 1 byte
        protected int isNeedRes;

        // 终端ID
        protected String devId;

        public int getIsNeedRes() { return isNeedRes; }
        public void setIsNeedRes(int isNeedRes) {
            this.isNeedRes = isNeedRes;
        }

        public String getDevId() {return devId;}
        public void setDevId(String devId) {
            this.devId = devId;
        }
    }
    /*
     * 功能类型及指令数据
     */
    public static class MsgType {
        // 功能类型编码
        protected String typeCode;

        // 功能项关键字
        protected String typeKeyword;

        // 指令数据
        protected String dataContent;

        public String getTypeCode() { return typeCode; }
        public void setTypeCode(String typeCode) {
            this.typeCode = typeCode;
        }

        public String getTypeKeyword() {return typeKeyword;}
        public void setTypeKeyword(String typeKeyword) {
            this.typeKeyword = typeKeyword;
        }

        public String getDataContent() { return dataContent; }
        public void setDataContent(String dataContent) {
            this.dataContent = dataContent;
        }

    }
}
