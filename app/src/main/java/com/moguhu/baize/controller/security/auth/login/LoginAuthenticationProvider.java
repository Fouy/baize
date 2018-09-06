package com.moguhu.baize.controller.security.auth.login;

import com.alibaba.fastjson.JSON;
import com.moguhu.baize.common.utils.auth.PasswdUtil;
import com.moguhu.baize.common.vo.user.UserRole;
import com.moguhu.baize.controller.security.model.UserContext;
import com.moguhu.baize.metadata.entity.user.UserEntity;
import com.moguhu.baize.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证提供者
 *
 * @author xuefeihu
 */
@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {

    private Logger logger = LoggerFactory.getLogger(LoginAuthenticationProvider.class);

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");
        logger.debug("[authentication info] - [{}]", JSON.toJSONString(authentication));
        String username = (String) authentication.getPrincipal();
        LoginRequest loginRequest = (LoginRequest) authentication.getCredentials();

        UserEntity user = userService.findByName(username);
        if (user == null) throw new UsernameNotFoundException("User not found: " + username);

        if (!StringUtils.equals(PasswdUtil.encode(loginRequest.getPassword()), user.getPassword())) {
            throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
        }
        List<UserRole> roles = userService.getRoleByUser(user);
        if (roles == null || roles.size() <= 0)
            throw new InsufficientAuthenticationException("User has no roles assigned");

        List<GrantedAuthority> authorities = roles.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.authority()))
                .collect(Collectors.toList());

        UserContext userContext = UserContext.create(user.getUserName(), authorities);
        userContext.setUserid(user.getUserId());
        userContext.setRealname(user.getRealName());

        return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
