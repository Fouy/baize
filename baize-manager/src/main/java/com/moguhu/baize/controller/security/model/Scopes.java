package com.moguhu.baize.controller.security.model;

/**
 * Scopes
 *
 * @author xuefeihu
 */
public enum Scopes {

    REFRESH_TOKEN;

    public String authority() {
        return "ROLE_" + this.name();
    }

}
