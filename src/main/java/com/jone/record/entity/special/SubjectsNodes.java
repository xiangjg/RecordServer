package com.jone.record.entity.special;

import com.jone.record.entity.file.FileEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "t_qzt_subjects_nodes", schema = "public")
public class SubjectsNodes implements Serializable, Comparable<SubjectsNodes> {
    private Integer id;
    private Integer sid;
    private Integer  type;
    private String style;
    private Integer  state;
    private String name;
    private Integer order;
    private String img;
    private String ident;
    private List<NodeContent> listContent;
    private List<FileEntity> files;

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

    @Basic
    @Column(name = "sid")
    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    @Basic
    @Column(name = "_type")
    public Integer  getType() {
        return type;
    }

    public void setType(Integer  type) {
        this.type = type;
    }

    @Basic
    @Column(name = "style")
    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Basic
    @Column(name = "state")
    public Integer  getState() {
        return state;
    }

    public void setState(Integer  state) {
        this.state = state;
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
    @Column(name = "_order")
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Basic
    @Column(name = "img")
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
    @Transient
    public List<NodeContent> getListContent() {
        return listContent;
    }

    public void setListContent(List<NodeContent> listContent) {
        this.listContent = listContent;
    }
    @Transient
    public List<FileEntity> getFiles() {
        return files;
    }

    public void setFiles(List<FileEntity> files) {
        this.files = files;
    }
    @Transient
    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    @Override
    public int compareTo(SubjectsNodes o) {
        return this.order - o.getOrder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubjectsNodes that = (SubjectsNodes) o;
        return id == that.id &&
                sid == that.sid &&
                type == that.type &&
                state == that.state &&
                Objects.equals(style, that.style) &&
                Objects.equals(name, that.name) &&
                Objects.equals(order, that.order) &&
                Objects.equals(img, that.img);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sid, type, style, state, name, order, img);
    }
}
