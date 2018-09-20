package com.moguhu.baize.common.utils;

/**
 * 十六进制转换
 */
public class HexUtil {

    /**
     * 十六进制字符串转字节数组
     *
     * @param hexstr 十六进制字符串
     * @return 字节数组
     */
    public static byte[] hexstr2Bytes(String hexstr) {
        String upper = hexstr.toUpperCase();
        int length = upper.length() / 2;
        byte[] ret = new byte[length];
        for (int i = 0; i < length; i++) {
            byte high = (byte) ("0123456789ABCDEF".indexOf(upper.charAt(2 * i)));
            byte low = (byte) ("0123456789ABCDEF".indexOf(upper.charAt(2 * i + 1)));
            ret[i] = (byte) ((high << 4) + low);
        }
        return ret;
    }

    /**
     * 字节数组转十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    public static String bytes2Hexstr(byte[] bytes) {
        String ret = "";
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

}
