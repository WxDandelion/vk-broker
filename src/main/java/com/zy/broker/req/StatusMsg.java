package com.zy.broker.req;

import java.util.ArrayList;
import java.util.List;

public class StatusMsg {
    // 状态/报警 &B类的解析
    private List<String> statusType;

    public List<String> getStatusType() {
        return statusType;
    }
    public void setStatusType(int[] data) {
        // 信息转换
        // bit位对应关系不确定是否正确
        String[] status0Type = new String[]{"总线故障", "GSM故障", "GPS模块故障", "锁车电路故障"};
        String[] status1Type = new String[]{"重车（载客）", "车门开", "空调开", "私密状态"};
        String[] status2Type = new String[]{"右后门开", "左后门开", "右前门开", "左前门开", "车窗开", "门锁关", "门锁开", "车窗开",
                "紧急报警/SOS/劫警", "盗警/非法进入报警", "震动报警", "碰撞报警", "进范围报警", "出范围报警", "超速报警", "偏离路线报警", "非法时段行驶报警",
                "停车休息时间不足报警", "位移报警/非法移动报警/越站报警", "非法开车门", "暗锁报警", "断电报警/剪线报警", "外部电瓶电压低报警", "推车报警",
                "停车未熄火报警/禁行报警", "急加速报警", "急减速报警", "冷却液温度过高报警"};
        List<String> list= new ArrayList<>();
        for(int i = 0; i < status0Type.length; i++) {
            if(data[i] == 1) {
                list.add(status0Type[i]);
            }
        }
        if(data[4] == 1) {
            list.add("ACC引擎启动");
        } else {
            list.add("ACC引擎熄火");
        }
        for(int i = 5; i < 5 + status1Type.length; i++) {
            if(data[i] == 1) {
                list.add(status1Type[i - 5]);
            }
        }
        if(data[9] == 0) {
            if(data[10] == 0) {
                list.add("GPS天线正常");
            } else {
                list.add("GPS天线短路");
            }
        } else {
            if(data[10] == 0) {
                list.add("GPS天线断路");
            } else {
                list.add("GPS天线未知状态");
            }
        }
        if(data[11] == 1) {
            list.add("设防");
        } else {
            list.add("撤防");
        }
        for(int i = 12; i < 12 + status2Type.length; i++) {
            if(data[i] == 1) {
                list.add(status2Type[i - 12]);
            }
        }
        this.statusType = list;
    }
}
