package com.jone.record.entity.system;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "t_zhgl_log", schema = "public")
public class SystemLog implements Serializable {
    private Long id;
    private Integer userId;
    private Integer menuId;
    private String inf;
    private Integer type;
    private Date tm;
    private String remark;
    private String ip;

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "menu_id")
    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    @Basic
    @Column(name = "inf")
    public String getInf() {
        return inf;
    }

    public void setInf(String inf) {
        this.inf = inf;
    }

    @Basic
    @Column(name = "_type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Basic
    @Column(name = "tm")
    public Date getTm() {
        return tm;
    }

    public void setTm(Date tm) {
        this.tm = tm;
    }

    @Basic
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "ip")
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemLog systemLog = (SystemLog) o;
        return id == systemLog.id &&
                Objects.equals(userId, systemLog.userId) &&
                Objects.equals(menuId, systemLog.menuId) &&
                Objects.equals(inf, systemLog.inf) &&
                Objects.equals(type, systemLog.type) &&
                Objects.equals(tm, systemLog.tm) &&
                Objects.equals(remark, systemLog.remark) &&
                Objects.equals(ip, systemLog.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, menuId, inf, type, tm, remark, ip);
    }
}
