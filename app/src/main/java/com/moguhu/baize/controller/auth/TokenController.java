package com.moguhu.baize.controller.auth;

import com.moguhu.baize.common.vo.AjaxResult;
import com.moguhu.baize.common.vo.user.UserRole;
import com.moguhu.baize.controller.BaseController;
import com.moguhu.baize.controller.security.auth.token.extractor.TokenExtractor;
import com.moguhu.baize.controller.security.auth.token.verifier.TokenVerifier;
import com.moguhu.baize.controller.security.config.TokenProperties;
import com.moguhu.baize.controller.security.config.WebSecurityConfig;
import com.moguhu.baize.controller.security.exceptions.InvalidTokenException;
import com.moguhu.baize.controller.security.model.UserContext;
import com.moguhu.baize.controller.security.model.token.RawAccessToken;
import com.moguhu.baize.controller.security.model.token.RefreshToken;
import com.moguhu.baize.controller.security.model.token.Token;
import com.moguhu.baize.controller.security.model.token.TokenFactory;
import com.moguhu.baize.metadata.entity.user.UserEntity;
import com.moguhu.baize.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Token Controller
 *
 * @author xuefeihu
 */
@RestController
@RequestMapping("/api/auth")
public class TokenController extends BaseController {

    @Autowired
    private TokenProperties tokenProperties;
    @Autowired
    private TokenVerifier tokenVerifier;
    @Autowired
    private TokenFactory tokenFactory;
    @Autowired
    private TokenExtractor tokenExtractor;
    @Autowired
    private UserService userService;

    @RequestMapping("/refresh_token")
    public AjaxResult refreshToken(HttpServletRequest request) {
        try {
            String tokenPayload = tokenExtractor.extract(request.getHeader(WebSecurityConfig.TOKEN_HEADER_PARAM));
            RawAccessToken rawToken = new RawAccessToken(tokenPayload);
            RefreshToken refreshToken = RefreshToken.create(rawToken, tokenProperties.getSigningKey()).orElseThrow(() -> new InvalidTokenException("Token验证失败"));

            String jti = refreshToken.getJti();
            if (!tokenVerifier.verify(jti)) {
                throw new InvalidTokenException("Token验证失败");
            }

            String subject = refreshToken.getSubject();
            UserEntity user = Optional.ofNullable(userService.findByName(subject)).orElseThrow(() -> new UsernameNotFoundException("用户未找到: " + subject));
            List<UserRole> roles = Optional.ofNullable(userService.getRoleByUser(user)).orElseThrow(() -> new InsufficientAuthenticationException("用户没有分配角色"));

            List<GrantedAuthority> authorities = roles.stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.authority()))
                    .collect(Collectors.toList());

            UserContext userContext = UserContext.create(user.getUserName(), authorities);
            userContext.setUserid(user.getUserId());
            userContext.setRealname(user.getRealName());

            Token accessToken = tokenFactory.createAccessToken(userContext);
            return AjaxResult.success(accessToken);
        } catch (Exception e) {
            logger.error("刷新Token失败, e= {}", e);
            return AjaxResult.error("刷新Token失败");
        }
    }


}
