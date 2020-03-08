package com.zy.broker.req;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LocationMsg {

    // 定位信息标识位
    private String statusFlag;
    // 纬度
    private String latitude;
    // 经度
    private String longitude;
    // 速度
    private int speed;
    // 方向 0-359，正北为 0，顺时针
    private int direction;
    // 时间 YY-MM-DD-hh-mm-ss
    // GMT+8 时间，本标准中之后涉及的时间均采用此时区
    private Date time;

    private String date;

    public LocationMsg() {

    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(String statusFlag) {
        // 在此做标志位转换
        Map<String, String> map = new HashMap<String, String>(){
            {
                put("0", "西经、南纬、定位");
                put("1", "西经、南纬、非定位");
                put("2", "西经、北纬、定位");
                put("3", "西经、北纬、非定位");
                put("4", "东经、南纬、定位");
                put("5", "东经、南纬、非定位");
                put("6", "东经、北纬、定位");
                put("7", "东经、北纬、非定位");
                put("F", "直接解析");
            }
        };
        this.statusFlag = map.get(statusFlag);
    }

    @Override
    public String toString() {
        return "LocationMsg [statusFlag=" + statusFlag
                + ", latitude=" + latitude + ", longitude=" + longitude + ", speed="
                + speed + ", direction=" + direction + ", time=" + time + "]";
    }

    public JSONObject toJSON() {
        JSONObject ret = new JSONObject();
        ret.put("statusFlag", statusFlag);
        ret.put("latitude", latitude);
        ret.put("longitude", longitude);
        ret.put("speed", speed);
        ret.put("direction", direction);
        ret.put("time", time);
        return ret;
    }

}
