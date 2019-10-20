package com.jone.record.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "t_qzt_subjects", schema = "public", catalog = "fangzyDB")
public class TQztSubjectsEntity implements Serializable {
    private Integer id;
    private String name;
    private String desc;
    private String creator;
    private Integer state;
    private byte[] img;
    private Short order;

    @Basic
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
    @Column(name = "desc")
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
    @Column(name = "state")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Basic
    @Column(name = "img")
    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    @Basic
    @Column(name = "order")
    public Short getOrder() {
        return order;
    }

    public void setOrder(Short order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TQztSubjectsEntity that = (TQztSubjectsEntity) o;
        return id == that.id &&
                state == that.state &&
                Objects.equals(name, that.name) &&
                Objects.equals(desc, that.desc) &&
                Objects.equals(creator, that.creator) &&
                Arrays.equals(img, that.img) &&
                Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, desc, creator, state, order);
        result = 31 * result + Arrays.hashCode(img);
        return result;
    }
}
