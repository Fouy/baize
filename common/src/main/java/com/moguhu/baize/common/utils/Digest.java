/*
 * Copyright (C), 2002-2016
 * FileName: Digest.java
 * Author:   luwanchuan
 * Date:     2016年4月14日 上午10:42:47
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.moguhu.baize.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;


/**
 * 数据摘要，对传来的数据进行Md5数据摘要
 *
 * @author luwanchuan
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class Digest {

    private static Logger LOGGER = LoggerFactory.getLogger(Digest.class);

//    /**
//     * 直接对传来的字符串进行md5摘要 功能描述: <br>
//     * 〈功能详细描述〉
//     * 
//     * @param str
//     * @return
//     * @throws UnsupportedEncodingException
//     * @see [相关类/方法](可选)
//     * @since [产品/模块版本](可选)
//     */
//    public static String digest(String str) {
//        String md5 = null;
//        try {
//            LOGGER.debug("生成的字符串为:{}", str);
//            md5 = MD5.digest(str, "UTF-8");
//            LOGGER.debug("生成的Md5摘要为:{}", md5);
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
//        return md5;
//    }

    /**
     * md5qianm: <br>
     * 〈功能详细描述〉
     * @param signKey 签名key
     * @param str 签名字符串
     * @return
     * @throws UnsupportedEncodingException
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String digest(String signKey,String str) {
    	Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
		String sign = md5PasswordEncoder.encodePassword(str, signKey);
		return sign;
    }
    
    /**
     * 1.根据key对传来的map数据排序 2.生成a1=b1&a2=b2&a3=b3形式的字符串，排除某些字符串Key值 3.调用digest方法进行md5编码 功能描述: <br>
     * 〈功能详细描述〉
     * 
     * @param signKey 签名key
     * @param map 要排序的字符串
     * @param key 要排除的key值
     * @return
     * @throws UnsupportedEncodingException
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String digest(String signKey,Map<String, String> map, String... keys) {

        TreeMap<String, String> treeMap = treeMap(map, keys);
        return digest(signKey,mapToString(treeMap));
    }

    /**
     * 
     * 功能描述: <br>
     * 将map按key字符串排序的treeMap
     * 
     * @param map
     * @param keys
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static TreeMap<String, String> treeMap(Map<String, String> map, String... keys) {
        // 初始化字符串比较器
        Comparator<String> stringComparator = new StringComparator();

        TreeMap<String, String> treeMap = new TreeMap<String, String>(stringComparator);
        treeMap.putAll(map);
        // 移除非摘要的key
        for (String key : keys) {
            treeMap.remove(key);
        }
        return treeMap;
    }

    /**
     * 
     * 功能描述: <br>
     * 将map转成a1=b1&a2=b2&a3=b3形式的字符串
     * 
     * @param map
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String mapToString(Map<String, String> map) {
        StringBuilder result = new StringBuilder();
        for (Entry<String, String> entry : map.entrySet()) {
            String value = entry.getValue() == null ? "" : entry.getValue().trim();
            result.append(entry.getKey()).append(CodecConstants.EQ_SYMBOL).append(value)
                    .append(CodecConstants.AND_SYMBOL);
        }
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString().trim();
    }

}
