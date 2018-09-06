package com.moguhu.baize.common.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * 数据摘要，对传来的数据进行Md5数据摘要
 */
public class DigestUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(DigestUtil.class);

    /**
     * 直接对传来的字符串进行md5摘要 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String digest(String str) {
        String md5 = null;
        try {
            LOGGER.debug("生成的字符串为:{}", str);
            md5 = MD5.digest(str, "UTF-8");
            LOGGER.debug("生成的Md5摘要为:{}", md5);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return md5;
    }

    /**
     * 1.根据key对传来的map数据排序 2.生成a1=b1&a2=b2&a3=b3形式的字符串，排除某些字符串Key值
     * 3.调用digest方法进行md5编码 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param map 要排序的字符串
     * @param key 要排除的key值
     * @return
     * @throws UnsupportedEncodingException
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String digest(Map<String, String> map, String... keys) {

        TreeMap<String, String> treeMap = treeMap(map, keys);
        return digest(mapToString(treeMap));
    }

    /**
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
     * 功能描述: <br>
     * 将map按key字符串排序的treeMap
     *
     * @param map
     * @param keys
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static TreeMap<String, Object> treeObjectMap(Map<String, Object> map, String... keys) {
        // 初始化字符串比较器
        Comparator<String> stringComparator = new StringComparator();

        TreeMap<String, Object> treeMap = new TreeMap<String, Object>(stringComparator);
        treeMap.putAll(map);
        // 移除非摘要的key
        for (String key : keys) {
            treeMap.remove(key);
        }
        return treeMap;
    }

    /**
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

    /**
     * 功能描述: <br>
     * 将map转成a1=b1&a2=b2&a3=b3形式的字符串
     *
     * @param map
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String objMapToString(Map<String, Object> map) {
        StringBuilder result = new StringBuilder();
        for (Entry<String, Object> entry : map.entrySet()) {
            String value = "";
            if (entry.getValue() != null) {
                if (entry.getValue() instanceof String) {
                    value = entry.getValue().toString().trim();
                } else if (entry.getValue() instanceof JSONObject) {
                    value = JSON.toJSONString(entry.getValue()).trim();
                } else if (entry.getValue() instanceof Long) {
                    value = String.valueOf(entry.getValue()).trim();
                }
            }
            result.append(entry.getKey()).append(CodecConstants.EQ_SYMBOL).append(value)
                    .append(CodecConstants.AND_SYMBOL);
        }
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString().trim();
    }
}
