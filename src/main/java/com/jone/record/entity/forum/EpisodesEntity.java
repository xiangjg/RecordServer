package com.jone.record.entity.forum;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "t_fzjt_episodes", schema = "public")
public class EpisodesEntity implements Serializable {
    private Integer id;
    private String name;
    private Integer courseId;
    private String synopsis;
    private Date updateDt;
    private String url;
    private Integer order;
    private Integer playCnt;
    private Integer state;

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
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "course_id")
    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    @Basic
    @Column(name = "abstract")
    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    @Basic
    @Column(name = "update_dt")
    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
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
    @Column(name = "_order")
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Basic
    @Column(name = "play_cnt")
    public Integer getPlayCnt() {
        return playCnt;
    }

    public void setPlayCnt(Integer playCnt) {
        this.playCnt = playCnt;
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
        EpisodesEntity that = (EpisodesEntity) o;
        return id == that.id &&
                courseId == that.courseId &&
                order == that.order &&
                playCnt == that.playCnt &&
                state == that.state &&
                Objects.equals(name, that.name) &&
                Objects.equals(synopsis, that.synopsis) &&
                Objects.equals(updateDt, that.updateDt) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, courseId, synopsis, updateDt, url, order, playCnt, state);
    }
}
