package com.david.copyright.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author DavidQ
 * @since 2022-04-13
 */
@ApiModel(value="Trade对象", description="")
public class Trade implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "交易单号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer saleid;

    @ApiModelProperty(value = "购买方")
    private String buyid;

    @ApiModelProperty(value = "售卖方")
    private String sellid;

    @ApiModelProperty(value = "作品号")
    private String workid;

    @ApiModelProperty(value = "交易时间")
    private LocalDateTime tradetime;

    @ApiModelProperty(value = "交易货价格")
    private BigDecimal tradeprice;

    private String txhash;

    private String tradecertification;

    private Boolean iscomment;

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getIscomment() {
        return iscomment;
    }

    public void setIscomment(Boolean iscomment) {
        this.iscomment = iscomment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getSaleid() {
        return saleid;
    }

    public void setSaleid(Integer saleid) {
        this.saleid = saleid;
    }
    public String getBuyid() {
        return buyid;
    }

    public void setBuyid(String buyid) {
        this.buyid = buyid;
    }
    public String getSellid() {
        return sellid;
    }

    public void setSellid(String sellid) {
        this.sellid = sellid;
    }
    public String getWorkid() {
        return workid;
    }

    public void setWorkid(String workid) {
        this.workid = workid;
    }
    public LocalDateTime getTradetime() {
        return tradetime;
    }

    public void setTradetime(LocalDateTime tradetime) {
        this.tradetime = tradetime;
    }
    public BigDecimal getTradeprice() {
        return tradeprice;
    }

    public void setTradeprice(BigDecimal tradeprice) {
        this.tradeprice = tradeprice;
    }
    public String getTxhash() {
        return txhash;
    }

    public void setTxhash(String txhash) {
        this.txhash = txhash;
    }
    public String getTradecertification() {
        return tradecertification;
    }

    public void setTradecertification(String tradecertification) {
        this.tradecertification = tradecertification;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
