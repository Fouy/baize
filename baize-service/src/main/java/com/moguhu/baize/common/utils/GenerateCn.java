package com.moguhu.baize.common.utils;

import java.util.Random;

/**
 * 生成随机编码或者数字
 *
 * @author xuefeihu
 */
public class GenerateCn {

    public static String generateNum(Integer len) {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < len; i++) {
            result += random.nextInt(10);
        }
        return result;
    }
}
