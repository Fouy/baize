package com.moguhu.baize.controller.security.model.token;

import io.jsonwebtoken.Claims;

/**
 * 验证Token
 *
 * @author xuefeihu
 */
public final class AccessToken implements Token {

    private final String rawToken;

    private Claims claims;

    protected AccessToken(final String token, Claims claims) {
        this.rawToken = token;
        this.claims = claims;
    }

    public String getToken() {
        return this.rawToken;
    }

    public Claims getClaims() {
        return claims;
    }
}
