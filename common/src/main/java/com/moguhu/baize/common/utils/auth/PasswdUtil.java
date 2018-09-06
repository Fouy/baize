package com.moguhu.baize.common.utils.auth;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * 密码工具类
 * <p>
 * Created by xuefeihu on 18/8/29.
 */
public class PasswdUtil {

    public static final String PWD_KEY = "baize";

    public static Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();

    /**
     * 生成摘要
     *
     * @param password
     * @return
     */
    public static String encode(String password) {
        return passwordEncoder.encodePassword(password, PWD_KEY);
    }

    public static void main(String[] args) {
        String test = PasswdUtil.encode("test");
        System.out.printf(test);
    }

}
