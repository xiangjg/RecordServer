

package com.jone.record.kbase.entity;

import java.util.List;

public class Catalog {

    // 记录ID
    private String id;

    // 目录标题
    private String title;

    // 当前节点ID
    private String guid;

    // 父节点ID
    private String parentGuid;

    // 孩子节点
    private List<Catalog> children;

    // 默认构造函数
    public Catalog() {
        super ();
    }

    // 初始化构造函数
    public Catalog(String id, String title, String guid, String parentGuid, List<Catalog> children) {
        this.id = id;
        this.title = title;
        this.guid = guid;
        this.parentGuid = parentGuid;
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getParentGuid() {
        return parentGuid;
    }

    public void setParentGuid(String parentGuid) {
        this.parentGuid = parentGuid;
    }

    public List<Catalog> getChildren() {
        return children;
    }

    public void setChildren(List<Catalog> children) {
        this.children = children;
    }
}
