package com.jone.record.entity.vo;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class UserInfo {
    private String loginName;
    private String session;
    private Date dt;
    private Integer roleId;
    private String proId;
    private String userId;
    private String userName;
    private BigInteger rights;
    private List<Integer> jobGroups;

    public BigInteger getRights() {
        return rights;
    }

    public void setRights(BigInteger rights) {
        this.rights = rights;
    }

    public List<Integer> getJobGroups() {
        return jobGroups;
    }

    public void setJobGroups(List<Integer> jobGroups) {
        this.jobGroups = jobGroups;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
