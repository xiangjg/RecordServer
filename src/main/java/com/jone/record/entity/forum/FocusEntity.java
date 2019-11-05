package com.jone.record.entity.forum;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "t_fzjt_focus", schema = "public")
public class FocusEntity implements Serializable {
    private Integer id;
    private String url;
    private Date startDt;
    private Date endDt;
    private Integer order;

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
    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "start_dt")
    public Date getStartDt() {
        return startDt;
    }

    public void setStartDt(Date startDt) {
        this.startDt = startDt;
    }

    @Basic
    @Column(name = "end_dt")
    public Date getEndDt() {
        return endDt;
    }

    public void setEndDt(Date endDt) {
        this.endDt = endDt;
    }

    @Basic
    @Column(name = "order")
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FocusEntity that = (FocusEntity) o;
        return id == that.id &&
                order == that.order &&
                Objects.equals(url, that.url) &&
                Objects.equals(startDt, that.startDt) &&
                Objects.equals(endDt, that.endDt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, startDt, endDt, order);
    }
}
