package com.moguhu.baize;

import com.moguhu.baize.common.utils.auth.PasswdUtil;

/**
 * Created by xuefeihu on 18/8/30.
 */
public class Test {

    public static void main(String[] args) {
        String encode = PasswdUtil.encode("test");
        System.out.printf(encode);
    }

}
