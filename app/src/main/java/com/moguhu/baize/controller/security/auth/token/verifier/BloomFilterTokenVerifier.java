package com.moguhu.baize.controller.security.auth.token.verifier;

import org.springframework.stereotype.Component;

/**
 * Token验证过滤器
 *
 * @author xuefeihu
 */
@Component
public class BloomFilterTokenVerifier implements TokenVerifier {

    @Override
    public boolean verify(String jti) {
        return true;
    }

}
