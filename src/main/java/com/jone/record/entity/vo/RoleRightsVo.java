package com.jone.record.entity.vo;


import java.util.List;

public class RoleRightsVo {

    private Integer id;
    private String type;
    private List<Integer> checkArr;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getCheckArr() {
        return checkArr;
    }

    public void setCheckArr(List<Integer> checkArr) {
        this.checkArr = checkArr;
    }
}
