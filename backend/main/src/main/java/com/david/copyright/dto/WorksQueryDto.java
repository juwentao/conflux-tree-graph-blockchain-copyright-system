package com.david.copyright.dto;

import com.alibaba.fastjson.JSON;
import com.david.copyright.entity.BasePageEntity;

public class WorksQueryDto extends BasePageEntity {
    private String userId;

    private  String workname;

    private Integer type;

    public String getWorkname() {
        return workname;
    }

    public void setWorkname(String workname) {
        this.workname = workname;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
