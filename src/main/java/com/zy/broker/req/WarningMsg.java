package com.zy.broker.req;

public class WarningMsg{
    // 报警类型
    private String warningType;
    // GPS相关
    private LocationMsg locationMsg;
    // 状态相关
    private StatusMsg statusMsg;

    public String getWarningType() {
        return warningType;
    }
    public void setWarningType(String warningType) {
        // 报警信息转换
        String[] warningInfo = new String[]{"防拆报警", "见光报警", "磁控报警", "蓝牙断开连接报警"};
        int type = Integer.parseInt(warningType);
        this.warningType = warningInfo[type];
    }

    public LocationMsg getLocationMsg() {
        return locationMsg;
    }

    public void setLocationMsg(LocationMsg locationMsg) {
        this.locationMsg = locationMsg;
    }

    public StatusMsg getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(StatusMsg statusMsg) {
        this.statusMsg = statusMsg;
    }
}
