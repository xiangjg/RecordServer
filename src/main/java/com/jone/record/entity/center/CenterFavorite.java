package com.jone.record.entity.center;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "t_user_center_favorite", schema = "public")
public class CenterFavorite implements Serializable {
    private Integer id;
    private String uid;
    private Integer type;
    private String detail;
    private Integer state;
    private Date insertDt;

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "uid")
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
    @Column(name = "detail")
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Basic
    @Column(name = "_state")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Basic
    @Column(name = "insert_dt")
    public Date getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(Date insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CenterFavorite that = (CenterFavorite) o;
        return id == that.id &&
                type == that.type &&
                state == that.state &&
                Objects.equals(uid, that.uid) &&
                Objects.equals(detail, that.detail) &&
                Objects.equals(insertDt, that.insertDt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uid, type, detail, state, insertDt);
    }
}
