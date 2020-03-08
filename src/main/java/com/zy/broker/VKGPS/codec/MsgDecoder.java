package com.zy.broker.VKGPS.codec;

import com.zy.broker.VKGPS.PackageData;
import com.zy.broker.VKGPS.PackageData.ResSetting;
import com.zy.broker.VKGPS.PackageData.MsgType;
import com.zy.broker.req.LocationMsg;
import com.zy.broker.req.StatusMsg;
import com.zy.broker.req.WarningMsg;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.zy.broker.VKGPS.util.StringUtils.stringToBineryArr;

@Slf4j
public class MsgDecoder {
    public PackageData String2PackageData(String data) {
        PackageData ret = new PackageData();

        String[] dataArr=data.split(",");

        // 1. 回复属性
        ResSetting resSetting = this.parseResSettingFromString(dataArr[0]);

        // 2. 功能及指令
        MsgType msgType = this.parseMsgTypeFromString(dataArr[1]);

        ret.setResSetting(resSetting);
        ret.setMsgType(msgType);

        // 0. 终端套接字地址信息
        // ret.setChannel(msg.getChannel());

        return ret;
    }

    private ResSetting parseResSettingFromString(String data) {
        ResSetting resSetting = new ResSetting();
        // 1. 回复属性
        resSetting.setIsNeedRes(Integer.parseInt(data.substring(0, 1)));
        // 2. 终端ID
        resSetting.setDevId(data.substring(1));

        return resSetting;
    }

    private MsgType parseMsgTypeFromString(String data) {
        MsgType msgType = new MsgType();

        // 1. 功能类型编码
        msgType.setTypeCode(data.substring(0, 1));
        // 2. 功能项关键字
        msgType.setTypeKeyword(data.substring(1, 2));
        // 3. 指令内容
        msgType.setDataContent(data.substring(2));

        return msgType;
    }

    private LocationMsg processGPS(String data) {
        LocationMsg ret = new LocationMsg();
        // 1.  状态
        ret.setStatusFlag(data.substring(23, 24));
        // 2.  纬度
        ret.setLatitude(data.substring(6, 8) + "度" + data.substring(8, 10) + "." + data.substring(10, 14));
        // 3.  经度
        ret.setLongitude(data.substring(14, 17) + "度" + data.substring(17, 19) + "." + data.substring(19, 23));
        // 4.  速度
        ret.setSpeed(Integer.parseInt(data.substring(24, 26)));
        // 5. 方向 0-359，正北为 0，顺时针
        ret.setDirection(Integer.parseInt(data.substring(26, 28)));
        // 6. 时间 YY-MM-DD-hh-mm-ss
        // GMT+8 时间，本标准中之后涉及的时间均采用此时区

        String dateStr = data.substring(32, 34) + data.substring(30, 32) + data.substring(28, 30) + data.substring(0, 6);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyMMddHHmmss").parse(dateStr);
        } catch (ParseException e) {
            log.error("",e);
        }
        ret.setTime(date);
        ret.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        return ret;
    }

    private StatusMsg processStatus(String data) {
        // &B类的解析
        int[] arr = stringToBineryArr(data);
        StatusMsg ret = new StatusMsg();
        ret.setStatusType(arr);
        return ret;
    }

    public WarningMsg toWarningMsg(PackageData packageData) {
        String data = packageData.getMsgType().getDataContent();
        WarningMsg ret = new WarningMsg();
        String[] dataArr = data.split("&");
        ret.setLocationMsg(processGPS(dataArr[1].substring(1)));
        ret.setWarningType(dataArr[0]);
        ret.setStatusMsg(processStatus(dataArr[2].substring(1)));
        return ret;
    }

    public LocationMsg toLocationMsg(PackageData packageData) {
        String data = packageData.getMsgType().getDataContent();
        LocationMsg gpsMsg = processGPS(data.substring(2));
        // location类 也有&B类，未加
        return gpsMsg;
    }

}
