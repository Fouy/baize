package com.moguhu.baize.controller.security.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * 用户上下文
 *
 * @author xuefeihu
 */
public class UserContext {

    public static final String CONTEXT = "context";

    /**
     * 用户ID
     */
    private Long userid;

    /**
     * 登录账号
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 角色
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private List<GrantedAuthority> authorities;

    public UserContext() {
    }

    private UserContext(String username, List<GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    public static UserContext create(String username, List<GrantedAuthority> authorities) {
        if (StringUtils.isBlank(username)) throw new IllegalArgumentException("Username is blank: " + username);
        return new UserContext(username, authorities);
    }

    public String getUsername() {
        return username;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public static String getCONTEXT() {
        return CONTEXT;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
