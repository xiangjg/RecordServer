package com.jone.record.entity.map;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "t_zhdt_share", schema = "public")
public class ShareEntity implements Serializable {
    private Integer id;
    private String creator;
    private String title;
    private String desc;
    private String img;
    private Date insertDt;
    private String auditId;
    private Date auditDt;
    private Integer state;

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic
    @Id
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "creator")
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "_desc")
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Basic
    @Column(name = "img")
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Basic
    @Column(name = "insert_dt")
    public Date getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(Date insertDt) {
        this.insertDt = insertDt;
    }

    @Basic
    @Column(name = "audit_id")
    public String getAuditId() {
        return auditId;
    }

    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    @Basic
    @Column(name = "audit_dt")
    public Date getAuditDt() {
        return auditDt;
    }

    public void setAuditDt(Date auditDt) {
        this.auditDt = auditDt;
    }

    @Basic
    @Column(name = "state")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShareEntity that = (ShareEntity) o;
        return id == that.id &&
                state == that.state &&
                Objects.equals(creator, that.creator) &&
                Objects.equals(title, that.title) &&
                Objects.equals(desc, that.desc) &&
                Objects.equals(img, that.img) &&
                Objects.equals(insertDt, that.insertDt) &&
                Objects.equals(auditId, that.auditId) &&
                Objects.equals(auditDt, that.auditDt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creator, title, desc, img, insertDt, auditId, auditDt, state);
    }
}
