package com.moguhu.baize.controller.security.auth.token.extractor;

/**
 * 实现这个接口应该返回原Base-64编码
 * 表示Token
 *
 * @author xuefeihu
 */
public interface TokenExtractor {

    String extract(String payload);

}
