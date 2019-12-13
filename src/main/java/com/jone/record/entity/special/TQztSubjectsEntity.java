package com.jone.record.entity.special;

import com.jone.record.entity.file.FileEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "t_qzt_subjects", schema = "public", catalog = "fangzyDB")
public class TQztSubjectsEntity implements Serializable {
    private Integer id;
    private String name;
    private String desc;
    private String creator;
    private Integer state;
    private Short num;
    private Date createDate;
    private List<FileEntity> files;
    private List<SubjectsNodes> listNode;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "create_dt")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
    @Column(name = "_desc")
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
    @Column(name = "_order")
    public Short getNum() {
        return num;
    }

    public void setNum(Short order) {
        this.num = num;
    }
    @Transient
    public List<FileEntity> getFiles() {
        return files;
    }

    public void setFiles(List<FileEntity> files) {
        this.files = files;
    }
    @Transient
    public List<SubjectsNodes> getListNode() {
        return listNode;
    }

    public void setListNode(List<SubjectsNodes> listNode) {
        this.listNode = listNode;
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
                Objects.equals(num, that.num);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, desc, creator, state, num);
        return result;
    }
}
