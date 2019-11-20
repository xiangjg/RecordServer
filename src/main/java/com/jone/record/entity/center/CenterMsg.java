package com.jone.record.entity.center;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "t_user_center_msg", schema = "public")
public class CenterMsg implements Serializable {
    private Integer msgId;
    private String fromId;
    private String toId;
    private Integer type;
    private String title;
    private String body;
    private Integer state;
    private Date insertDt;
    private Date readDt;

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "msg_id")
    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    @Basic
    @Column(name = "from_id")
    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    @Basic
    @Column(name = "to_id")
    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
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
    @Column(name = "body")
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    @Basic
    @Column(name = "read_dt")
    public Date getReadDt() {
        return readDt;
    }

    public void setReadDt(Date readDt) {
        this.readDt = readDt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CenterMsg centerMsg = (CenterMsg) o;
        return msgId == centerMsg.msgId &&
                type == centerMsg.type &&
                state == centerMsg.state &&
                Objects.equals(fromId, centerMsg.fromId) &&
                Objects.equals(toId, centerMsg.toId) &&
                Objects.equals(title, centerMsg.title) &&
                Objects.equals(body, centerMsg.body) &&
                Objects.equals(insertDt, centerMsg.insertDt) &&
                Objects.equals(readDt, centerMsg.readDt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(msgId, fromId, toId, type, title, body, state, insertDt, readDt);
    }
}
