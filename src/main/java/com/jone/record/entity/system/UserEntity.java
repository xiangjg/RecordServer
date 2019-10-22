package com.jone.record.entity.system;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "t_zhgl_users", schema = "public", catalog = "fangzyDB")
public class UserEntity implements Serializable {
    private Integer id;
    private String name;
    private Integer departId;
    private String duty;
    private RoleEntity role;
    private String tel;
    private String email;
    private String remark;
    private Timestamp regDt;
    private Integer state;
    private String loginName;
    private String password;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "depart_id")
    public Integer getDepartId() {
        return departId;
    }

    public void setDepartId(Integer departId) {
        this.departId = departId;
    }

    @Basic
    @Column(name = "duty")
    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    @Basic
    @JoinColumn(name = "role_id")
    @ManyToOne(fetch = FetchType.EAGER)
    public RoleEntity getRole() {
        return role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }

    @Basic
    @Column(name = "tel")
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
    @Column(name = "reg_dt")
    public Timestamp getRegDt() {
        return regDt;
    }

    public void setRegDt(Timestamp regDt) {
        this.regDt = regDt;
    }

    @Basic
    @Column(name = "state")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Basic
    @Column(name = "login_name")
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id &&
                state == that.state &&
                Objects.equals(name, that.name) &&
                Objects.equals(departId, that.departId) &&
                Objects.equals(duty, that.duty) &&
                Objects.equals(tel, that.tel) &&
                Objects.equals(email, that.email) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(regDt, that.regDt) &&
                Objects.equals(loginName, that.loginName) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, departId, duty,  tel, email, remark, regDt, state, loginName, password);
    }
}
