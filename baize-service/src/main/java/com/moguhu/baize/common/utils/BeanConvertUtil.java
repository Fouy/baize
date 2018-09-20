/*
 * Copyright (C), 2002-2016
 * FileName: BeanConvertUtil.java
 * Author:   luwanchuan
 * Date:     2016年4月20日 上午11:27:52
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.moguhu.baize.common.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Iterator;
import java.util.Map;

/**
 * bean<->map 转换
 *
 * @author luwanchuan
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class BeanConvertUtil {

    /**
     * java bean -> map <br>
     * bean中所有属性都转化为map的键值对，包含属性值为null或空值的
     *
     * @param object bean
     * @return map，包含bean中所有属性的键值对，包含 属性值为null或空值的
     */
    public static Map<String, String> beanToMap(Object object) {
        Map<String, String> map = null;
        try {
            map = BeanUtils.describe(object);
            map.remove("class");
        } catch (Exception e) {
            throw new RuntimeException("beanToMap转换发生错误", e);
        }
        return map;
    }

    /**
     * java bean -> map <br>
     * bean中所有属性都转化为map的键值对，但结果map中<已经删除> 属性值为null或空值所对应的键值对
     *
     * @param object
     * @return map，包含bean中所有属性的键值对，但是 <删除> 属性值为null或空值所对应的键值对
     */
    public static Map<String, String> beanToMapWithoutNullValueMap(Object object) {
        Map<String, String> map = null;
        try {
            map = BeanUtils.describe(object);
            map.remove("class");
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                if (StringUtils.isBlank(entry.getValue())) {
                    it.remove();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("beanToMap转换发生错误", e);
        }

        return map;
    }

    /**
     * map -> java bean <br>
     *
     * @param map
     * @param type
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static <T> T mapToBean(Map map, Class<T> type) {

        T bean = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
            bean = type.newInstance(); // 创建 JavaBean 对象

            // 给 JavaBean 对象的属性赋值
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();

                if (map.containsKey(propertyName)) {
                    // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                    Object value = map.get(propertyName);

                    Object[] args = new Object[1];
                    args[0] = value;
                    descriptor.getWriteMethod().invoke(bean, args);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("mapToBean转换发生错误", e);
        }
        return bean;
    }

}
