package com.moguhu.baize.controller.security.auth.token.verifier;

/**
 * Token验证
 *
 * @author xuefeihu
 */
public interface TokenVerifier {

    boolean verify(String jti);

}
