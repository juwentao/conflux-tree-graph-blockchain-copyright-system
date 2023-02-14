package com.david.copyright.vo;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

public class SaleVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer sid;

    private String workid;

    @ApiModelProperty(value = "1为在售")
    private Integer status;

    @ApiModelProperty(value = "价格两位小数")
    private BigDecimal price;

    @ApiModelProperty(value = "售品描述")
    private String description;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "1.摄影作品 2.文字作品 3.影视作品 4.音乐作品 5.美术作品")
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ApiModelProperty(value = "下载量")
    private Integer downloads;

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getWorkid() {
        return workid;
    }

    public void setWorkid(String workid) {
        this.workid = workid;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
