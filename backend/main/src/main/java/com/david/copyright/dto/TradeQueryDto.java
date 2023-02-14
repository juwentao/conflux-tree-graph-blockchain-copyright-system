package com.david.copyright.dto;

import com.alibaba.fastjson.JSON;
import com.david.copyright.entity.BasePageEntity;

public class TradeQueryDto extends BasePageEntity {
    private Integer type;

    private  String workname;

    private String userId;

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getWorkname() {
        return workname;
    }

    public void setWorkname(String workname) {
        this.workname = workname;
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
