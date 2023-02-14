package com.david.copyright.response;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.david.copyright.error.BaseErrorInfoInterface;
import com.david.copyright.error.CommonEnum;


import java.beans.ConstructorProperties;
import java.io.Serializable;

/**
 * @author DavidQ
 * @version 1.0.0
 * @ClassName ResultBody.java
 * @Description 统一结果返回封装
 * @createTime 2021年05月08日 22:43:00
 */
public class ResultBody implements Serializable {
    /**
     * 响应代码
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应结果
     */
    private Object result;

    /**
     * 总条数
     */
    private Long total;

    /**
     * 分页信息
     */
    private PageInfo pageInfo;

    public static class PageInfo {

        //当前页
        protected int currentPage;
        //页大小
        protected int pageSize;
        //总记录数
        protected long totalCount;
        //总页数
        protected long totalPage;

        public PageInfo() {
        }

        @ConstructorProperties({"currentPage", "pageSize", "totalCount", "totalPage"})
        public PageInfo(int currentPage, int pageSize, long totalCount, long totalPage) {
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.totalCount = totalCount;
            this.totalPage = totalPage;
        }
    }

    public ResultBody() {
    }

    public ResultBody(BaseErrorInfoInterface errorInfo) {
        this.code = errorInfo.getResultCode();
        this.message = errorInfo.getResultMsg();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    /**
     * 成功
     *
     * @return
     */
    public static ResultBody success() {
        return success(null);
    }

    /**
     * 成功
     * @param result
     * @return
     */
    public static ResultBody success(Object result) {
        ResultBody rb = new ResultBody();
        rb.setCode(CommonEnum.SUCCESS.getResultCode());
        rb.setMessage(CommonEnum.SUCCESS.getResultMsg());
        rb.setResult(result);
        return rb;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    /**
     * 成功
     * @param code,message,result
     * @return
     */
    public static ResultBody success(String code, String message, Object result) {
        ResultBody rb = new ResultBody();
        rb.setCode(code);
        rb.setMessage(message);
        if (result instanceof Page<?>)
        {
            Page<?> page = (Page<?>) result;
            rb.setTotal(page.getTotal());
            rb.setResult(page.getRecords());
            rb.setPageInfo(new PageInfo((int)page.getCurrent(), (int)page.getSize(), page.getTotal(), page.getPages()));
        } else {
            rb.setResult(result);
        }
        return rb;
    }

    /**
     * 成功
     * @param code,message
     * @return
     */
    public static ResultBody success(String code, String message) {
        ResultBody rb = new ResultBody();
        rb.setCode(code);
        rb.setMessage(message);
        return rb;
    }

    /**
     * 失败
     */
    public static ResultBody error(BaseErrorInfoInterface errorInfo) {
        ResultBody rb = new ResultBody();
        rb.setCode(errorInfo.getResultCode());
        rb.setMessage(errorInfo.getResultMsg());
        rb.setResult(null);
        return rb;
    }

    /**
     * 失败
     */
    public static ResultBody error(String code, String message) {
        ResultBody rb = new ResultBody();
        rb.setCode(code);
        rb.setMessage(message);
        rb.setResult(null);
        return rb;
    }

    /**
     * 失败
     */
    public static ResultBody error( String message) {
        ResultBody rb = new ResultBody();
        rb.setCode("400");
        rb.setMessage(message);
        rb.setResult(null);
        return rb;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
