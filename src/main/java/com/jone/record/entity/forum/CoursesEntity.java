package com.jone.record.entity.forum;

import com.jone.record.entity.file.FileEntity;

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
    private CourseCategory category;
    private String synopsis;
    private String keyword;
    private String copyright;
    private String cover;
    private Integer playCnt;
    private Date createDt;
    private Date updateDt;
    private Integer state;
    private List<EpisodesEntity> episodesList;
    private List<FileEntity> files;

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

    @ManyToOne
    @JoinColumn(name = "cat_id")
    public CourseCategory getCategory() {
        return category;
    }

    public void setCategory(CourseCategory category) {
        this.category = category;
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
    @Transient
    public List<FileEntity> getFiles() {
        return files;
    }

    public void setFiles(List<FileEntity> files) {
        this.files = files;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoursesEntity that = (CoursesEntity) o;
        return id == that.id &&
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
        return Objects.hash(id, name,  synopsis, keyword, copyright, cover, playCnt, createDt, updateDt, state);
    }
}
