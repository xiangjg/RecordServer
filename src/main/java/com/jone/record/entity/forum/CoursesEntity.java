package com.jone.record.entity.forum;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "t_fzjt_courses", schema = "public")
public class CoursesEntity implements Serializable {
    private Integer id;
    private String name;
    private Integer catId;
    private String synopsis;
    private String keyword;
    private String copyright;
    private String cover;
    private Integer playCnt;
    private Date createDt;
    private Date updateDt;
    private Integer state;
    private List<EpisodesEntity> episodesList;

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
    @Column(name = "cat_id")
    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
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
    @Column(name = "keyword")
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Basic
    @Column(name = "copyright")
    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    @Basic
    @Column(name = "cover")
    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Basic
    @Column(name = "play_cnt")
    public int getPlayCnt() {
        return playCnt;
    }

    public void setPlayCnt(int playCnt) {
        this.playCnt = playCnt;
    }

    @Basic
    @Column(name = "create_dt")
    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
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
    @Column(name = "state")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
    @Transient
    public List<EpisodesEntity> getEpisodesList() {
        return episodesList;
    }

    public void setEpisodesList(List<EpisodesEntity> episodesList) {
        this.episodesList = episodesList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoursesEntity that = (CoursesEntity) o;
        return id == that.id &&
                catId == that.catId &&
                playCnt == that.playCnt &&
                state == that.state &&
                Objects.equals(name, that.name) &&
                Objects.equals(synopsis, that.synopsis) &&
                Objects.equals(keyword, that.keyword) &&
                Objects.equals(copyright, that.copyright) &&
                Objects.equals(cover, that.cover) &&
                Objects.equals(createDt, that.createDt) &&
                Objects.equals(updateDt, that.updateDt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, catId, synopsis, keyword, copyright, cover, playCnt, createDt, updateDt, state);
    }
}
