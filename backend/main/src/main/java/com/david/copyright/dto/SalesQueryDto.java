package com.david.copyright.dto;

import com.alibaba.fastjson.JSON;
import com.david.copyright.entity.BasePageEntity;
import io.swagger.models.auth.In;

public class SalesQueryDto  extends BasePageEntity {
    String content;
    Integer sortType;
    Integer workType;
    Integer status;
    String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public Integer getWorkType() {
        return workType;
    }

    public void setWorkType(Integer workType) {
        this.workType = workType;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
