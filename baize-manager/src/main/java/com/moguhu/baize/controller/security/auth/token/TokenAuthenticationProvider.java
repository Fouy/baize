package com.moguhu.baize.controller.security.auth.token;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.moguhu.baize.controller.security.auth.AuthenticationToken;
import com.moguhu.baize.controller.security.config.TokenProperties;
import com.moguhu.baize.controller.security.model.UserContext;
import com.moguhu.baize.controller.security.model.token.RawAccessToken;
import com.moguhu.baize.controller.security.model.token.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 使用 {@link AuthenticationProvider} 的接口提供实现 {@link Token} 身份验证的实例
 *
 * @author xuefeihu
 */
@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private Logger logger = LoggerFactory.getLogger(TokenAuthenticationProvider.class);

    @Autowired
    private TokenProperties tokenProperties;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RawAccessToken rawAccessToken = (RawAccessToken) authentication.getCredentials();

        long startTime = System.currentTimeMillis();
        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(tokenProperties.getSigningKey());
        logger.debug("[验证Token消耗时间] - [{}]", (System.currentTimeMillis() - startTime));

        @SuppressWarnings("unchecked")
        List<String> scopes = jwsClaims.getBody().get("scopes", List.class);
        List<GrantedAuthority> authorities = scopes.stream().map(SimpleGrantedAuthority::new).collect(toList());
        
        String contentStr = JSON.toJSONString(jwsClaims.getBody().get(UserContext.CONTEXT));
        UserContext context = JSON.parseObject(contentStr, new TypeReference<UserContext>(){});
        context.setAuthorities(authorities);
        return new AuthenticationToken(context, context.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (AuthenticationToken.class.isAssignableFrom(authentication));
    }
}
