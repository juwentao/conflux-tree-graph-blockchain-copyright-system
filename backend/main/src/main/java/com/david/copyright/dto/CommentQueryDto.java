package com.david.copyright.dto;

import com.alibaba.fastjson.JSON;
import com.david.copyright.entity.BasePageEntity;

public class CommentQueryDto extends BasePageEntity {
    Integer saleid;

    public Integer getSaleid() {
        return saleid;
    }

    public void setSaleid(Integer saleid) {
        this.saleid = saleid;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
