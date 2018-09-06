package com.moguhu.baize.service.auth;

/**
 * 验证码缓存服务
 *
 * @author xuefeihu
 */
public interface CodeCacheService {

    /**
     * 存入验证码
     *
     * @param codeId
     * @param verifyCode
     */
    void put(String codeId, String verifyCode);

    /**
     * 获取验证码
     * @param codeId
     * @return
     */
    String get(String codeId);

}
