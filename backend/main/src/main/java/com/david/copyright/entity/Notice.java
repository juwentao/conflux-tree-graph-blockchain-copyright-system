package com.david.copyright.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 *
 * </p>
 *
 * @author DavidQ
 * @since 2022-04-15
 */
@ApiModel(value="Notice对象", description="")
public class Notice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "发送方")
    private String sendName;

    @ApiModelProperty(value = "接收方")
    private String receiveName;

    public Notice() {
    }

    @ApiModelProperty(value = "内容")
    private String content;

    private LocalDateTime sendTime;

    public Notice(LocalDateTime sendTime, String sendName,String receiveName, String content, int isRead) {
//        this.sendTime= String.valueOf(sendTime);
        this.sendName=sendName;
        this.receiveName=receiveName;
        this.sendTime=sendTime;
        this.content=content;
        this.isRead=isRead;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public int getisRead() {
        return isRead;
    }

    public void setisRead(int isRead) {
        this.isRead = isRead;
    }

    private int isRead;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSendDelete() {
        return sendDelete;
    }

    public void setSendDelete(int sendDelete) {
        this.sendDelete = sendDelete;
    }

    public int getReceiveDelete() {
        return receiveDelete;
    }

    public void setReceiveDelete(int receiveDelete) {
        this.receiveDelete = receiveDelete;
    }

    private int sendDelete;
    private int receiveDelete;
    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }
    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "id=" + id +
                ", sendName=" + sendName +
                ", receiveName=" + receiveName +
                ", content=" + content +
                ", sendTime=" + sendTime +
                ", isRead=" + isRead +
                "}";
    }
}
