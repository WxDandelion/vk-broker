package com.zy.broker.VKGPS.util;

public class StringUtils {
    public static int[] stringToBineryArr(String data) {
        int[] arr = new int[40];
        String[] arrStr = data.split("");
        String tmp;
        for(int i = 0; i < arrStr.length; i++) {
            tmp = Integer.toBinaryString(Integer.parseInt(arrStr[i]));
            int tmpLen = tmp.length();
            if(tmpLen < 4) {
                for(int j = 0 ; j < 4 - tmpLen; j++) {
                    tmp = "0" + tmp;
                }
            }
            String[] tmpArr = tmp.split("");
            arr[4*i] = Integer.parseInt(tmpArr[0]);
            arr[4*i + 1] = Integer.parseInt(tmpArr[1]);
            arr[4*i + 2] = Integer.parseInt(tmpArr[2]);
            arr[4*i + 3] = Integer.parseInt(tmpArr[3]);
        }
        return arr;
    }
}
