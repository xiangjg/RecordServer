package com.jone.record.entity.center;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "t_user_center_bookmarks", schema = "public")
public class CenterBookmarks implements Serializable {
    private Integer id;
    private String uid;
    private Integer type;
    private String title;
    private String bookId;
    private String chapterId;
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
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "book_id")
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    @Basic
    @Column(name = "chapter_id")
    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
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
        CenterBookmarks that = (CenterBookmarks) o;
        return id == that.id &&
                type == that.type &&
                state == that.state &&
                Objects.equals(uid, that.uid) &&
                Objects.equals(title, that.title) &&
                Objects.equals(bookId, that.bookId) &&
                Objects.equals(chapterId, that.chapterId) &&
                Objects.equals(insertDt, that.insertDt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uid, type, title, bookId, chapterId, state, insertDt);
    }
}
