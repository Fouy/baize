/*
 * Copyright (C), 2002-2016
 * FileName: StringComparator.java
 * Author:   luwanchuan
 * Date:     2016年4月14日 上午10:32:49
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.moguhu.baize.common.utils;

import java.util.Comparator;

/**
 * 比较器
 *
 * @author luwanchuan
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class StringComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        return o1.compareTo(o2);
    }

}
