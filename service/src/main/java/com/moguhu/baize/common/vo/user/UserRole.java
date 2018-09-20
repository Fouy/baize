package com.moguhu.baize.common.vo.user;

/**
 * 用户角色
 *
 * @author xuefeihu
 */
public class UserRole {

    private String roleName;

    public UserRole() {
    }

    public UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String authority() {
        return this.getRoleName();
    }
}
