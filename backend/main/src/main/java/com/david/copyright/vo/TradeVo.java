package com.david.copyright.vo;
import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TradeVo implements Serializable{
    private static final long serialVersionUID = 1L;
    private String workname;
    private Integer type;
    private LocalDateTime tradetime;
    private BigDecimal tradeprice;
    private String filedownloadurl;
    private String txhash;
    private String tradecertification;
    private String filehash;
    private Integer status;
    private String filename;
    private Boolean iscomment;
    private Integer saleid;
    private Integer tradeid;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTradeid() {
        return tradeid;
    }

    public void setTradeid(Integer tradeid) {
        this.tradeid = tradeid;
    }

    public Integer getSaleid() {
        return saleid;
    }

    public void setSaleid(Integer saleid) {
        this.saleid = saleid;
    }

    public Boolean getIscomment() {
        return iscomment;
    }

    public void setIscomment(Boolean iscomment) {
        this.iscomment = iscomment;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

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

    public String getFiledownloadurl() {
        return filedownloadurl;
    }

    public void setFiledownloadurl(String filedownloadurl) {
        this.filedownloadurl = filedownloadurl;
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

    public String getFilehash() {
        return filehash;
    }

    public void setFilehash(String filehash) {
        this.filehash = filehash;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
