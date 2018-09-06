package com.moguhu.baize.metadata.entity.user;

import java.io.Serializable;
import java.util.Date;

public class UserEntity implements Serializable {
    /**
     * 主键ID.
     */
    private Long userId;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 更新时间.
     */
    private Date modifyTime;

    /**
     * 登录名.
     */
    private String userName;

    /**
     * 真实姓名.
     */
    private String realName;

    /**
     * 密码.
     */
    private String password;

    /**
     * 手机.
     */
    private String phone;

    /**
     * 电子邮件.
     */
    private String email;

    /**
     * 描述说明.
     */
    private String info;

    private static final long serialVersionUID = 1L;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}