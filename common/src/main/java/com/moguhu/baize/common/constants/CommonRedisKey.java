package com.moguhu.baize.common.constants;

/**
 * redis缓存key
 *
 * Created by xuefeihu on 18/5/2.
 *
 */
public class CommonRedisKey {

    public static final String PREFIX = "Baize:";

/////////////////////////////////////////////////////////////////////
// 登录相关
/////////////////////////////////////////////////////////////////////
    public static final String LOGIN = PREFIX + "Login:";

    /** 登录验证码 */
    public static final String LOGIN_CAPTCHA = LOGIN + "Captcha:";

}
