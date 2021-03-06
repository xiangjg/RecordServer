package com.jone.record.entity.special;

import com.jone.record.entity.file.FileEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "t_qzt_node_content", schema = "public")
public class NodeContent implements Serializable {
    private Integer  id;
    private Integer  nid;
    private Integer  type;
    private Integer  state;
    private String name;
    private String detail;
    private String content;
    private Integer  order;
    private String ident;
    private List<FileEntity> files;

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic
    @Id
    @Column(name = "id")
    public Integer  getId() {
        return id;
    }

    public void setId(Integer  id) {
        this.id = id;
    }

    @Basic
    @Column(name = "nid")
    public Integer  getNid() {
        return nid;
    }

    public void setNid(Integer  nid) {
        this.nid = nid;
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
    @Column(name = "detail")
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "_order")
    public Integer  getOrder() {
        return order;
    }

    public void setOrder(Integer  order) {
        this.order = order;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeContent that = (NodeContent) o;
        return id == that.id &&
                nid == that.nid &&
                type == that.type &&
                state == that.state &&
                order == that.order &&
                Objects.equals(name, that.name) &&
                Objects.equals(detail, that.detail) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int  hashCode() {
        return Objects.hash(id, nid, type, state, name, detail, content, order);
    }
}
