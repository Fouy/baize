package com.moguhu.baize.service.auth.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.moguhu.baize.common.constants.CommonRedisKey;
import com.moguhu.baize.service.auth.CodeCacheService;
import org.springframework.stereotype.Service;

/**
 * 验证码缓存服务
 *
 * Created by xuefeihu on 18/9/6.
 */
@Service
public class CodeCacheServiceImpl implements CodeCacheService {

    /**
     * 验证码失效时间 2分钟
     */
    private static final int CAPTCHA_KEY_TIME = 2 * 60;

    @CreateCache(name = CommonRedisKey.LOGIN_CAPTCHA, expire = CAPTCHA_KEY_TIME, cacheType = CacheType.REMOTE)
    private Cache<String, String> codeCache;


    @Override
    public void put(String codeId, String verifyCode) {
        codeCache.put(codeId, verifyCode);
    }

    @Override
    public String get(String codeId) {
        return codeCache.get(codeId);
    }
}
