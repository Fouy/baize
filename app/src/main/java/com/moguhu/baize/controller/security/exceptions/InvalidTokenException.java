package com.moguhu.baize.controller.security.exceptions;

/**
 * 无效的Token
 *
 * @author xuefeihu
 */
public class InvalidTokenException extends RuntimeException {

    private static final long serialVersionUID = -294671188037098603L;

    public InvalidTokenException(String msg) {
        super(msg);
    }

    public InvalidTokenException(String msg, Throwable t) {
        super(msg, t);
    }
}
