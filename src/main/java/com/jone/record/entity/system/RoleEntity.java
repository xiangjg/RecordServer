package com.jone.record.entity.system;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

@Entity
@Table(name = "t_zhgl_role", schema = "public", catalog = "fangzyDB")
public class RoleEntity implements Serializable {
    private Integer id;
    private String name;
    private BigInteger rights;
    private BigInteger add;
    private BigInteger del;
    private BigInteger change;
    private BigInteger query;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
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
    @Column(name = "rights")
    public BigInteger getRights() {
        return rights;
    }

    public void setRights(BigInteger rights) {
        this.rights = rights;
    }

    @Basic
    @Column(name = "add")
    public BigInteger getAdd() {
        return add;
    }

    public void setAdd(BigInteger add) {
        this.add = add;
    }

    @Basic
    @Column(name = "del")
    public BigInteger getDel() {
        return del;
    }

    public void setDel(BigInteger del) {
        this.del = del;
    }

    @Basic
    @Column(name = "change")
    public BigInteger getChange() {
        return change;
    }

    public void setChange(BigInteger change) {
        this.change = change;
    }

    @Basic
    @Column(name = "query")
    public BigInteger getQuery() {
        return query;
    }

    public void setQuery(BigInteger query) {
        this.query = query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleEntity that = (RoleEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(rights, that.rights) &&
                Objects.equals(add, that.add) &&
                Objects.equals(del, that.del) &&
                Objects.equals(change, that.change) &&
                Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, rights, add, del, change, query);
    }
}
