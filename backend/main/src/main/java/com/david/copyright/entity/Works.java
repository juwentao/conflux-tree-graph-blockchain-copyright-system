package com.david.copyright.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 *
 * </p>
 *
 * @author DavidQ
 * @since 2022-03-15
 */
@ApiModel(value="Works对象", description="")
public class Works implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "作品名称")
    private String workname;

    @ApiModelProperty(value = "上传此作品的用户id")
    private String userid;

    @ApiModelProperty(value = "1.摄影作品 2.文字作品 3.影视作品 4.音乐作品 5.美术作品")
    private Integer type;

    @ApiModelProperty(value = "作品简介")
    private String introduction;

    @ApiModelProperty(value = "作品文件存放位置")
    private String filelocation;

    @ApiModelProperty(value = "作品文件是否已发表 1代表已发表")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Boolean ispublished;

    @ApiModelProperty(value = "作品完成时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private LocalDateTime completiontime;

    @ApiModelProperty(value = "作品完成地点")
    private String completionlocation;

    @ApiModelProperty(value = "当前是否为作者，1为是")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Boolean isauthor;

    @ApiModelProperty(value = "当前作品是否存证 0为否 1为是 2为已转让 ")
    private Integer status;

    @ApiModelProperty(value = "是否进行权利声明 ")
    private Boolean isauthority;

    @ApiModelProperty(value = "当前作品记录创建时间")
    private LocalDateTime createtime;

    @ApiModelProperty(value = "作品文件名")
    private String filename;

    @ApiModelProperty(value = "作品文件下载链接")
    private String filedownloadurl;

    @ApiModelProperty(value = "作品作者名字")
    private String authorname;

    @ApiModelProperty(value = "作品作者身份证号")
    private String authoridnumber;


    @ApiModelProperty(value = "文件哈希值")
    private String filehash;

    @ApiModelProperty(value = "上链交易哈希值")
    private String chainhash;


    @ApiModelProperty(value = "发表日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private LocalDate publishedtime;

    @ApiModelProperty(value = "发表地点")
    private String publishedlocation;

    @ApiModelProperty(value = "著作权人名称")
    private String copyrightownername;

    @ApiModelProperty(value = "著作权人证件号码")
    private String copyrightownerid;

    @ApiModelProperty(value = "著作权人身份类别 1自然人 2企业法人 3.机关法人 4事业单位法人 5社会团体法人 6其他")
    private Integer copyrightownertype;

    @ApiModelProperty(value = "著作权人证件类型 1身份证号 2军人身份证明 3户口本 4统一社会信用代码证书 5其他有效证件")
    private Integer idtype;

    private String workuri;

    private String chaintime;

    private String blockheight;

    private String certificatefilelocation;

    public String getChaintime() {
        return chaintime;
    }

    public void setChaintime(String chaintime) {
        this.chaintime = chaintime;
    }

    public String getBlockheight() {
        return blockheight;
    }

    public void setBlockheight(String blockheight) {
        this.blockheight = blockheight;
    }

    public String getCertificatefilelocation() {
        return certificatefilelocation;
    }

    public void setCertificatefilelocation(String certificatefilelocation) {
        this.certificatefilelocation = certificatefilelocation;
    }

    public String getWorkuri() {
        return workuri;
    }

    public void setWorkuri(String workuri) {
        this.workuri = workuri;
    }

    public Boolean getIsauthority() {
        return isauthority;
    }

    public void setIsauthority(Boolean isauthority) {
        this.isauthority = isauthority;
    }

    public Integer getCopyrightownertype() {
        return copyrightownertype;
    }

    public void setCopyrightownertype(Integer copyrightownertype) {
        this.copyrightownertype = copyrightownertype;
    }

    public Integer getIdtype() {
        return idtype;
    }

    public void setIdtype(Integer idtype) {
        this.idtype = idtype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkname() {
        return workname;
    }

    public void setWorkname(String workname) {
        this.workname = workname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getFilelocation() {
        return filelocation;
    }

    public void setFilelocation(String filelocation) {
        this.filelocation = filelocation;
    }

    public Boolean getIspublished() {
        return ispublished;
    }

    public void setIspublished(Boolean ispublished) {
        this.ispublished = ispublished;
    }

    public LocalDateTime getCompletiontime() {
        return completiontime;
    }

    public void setCompletiontime(LocalDateTime completiontime) {
        this.completiontime = completiontime;
    }

    public String getCompletionlocation() {
        return completionlocation;
    }

    public void setCompletionlocation(String completionlocation) {
        this.completionlocation = completionlocation;
    }

    public Boolean getIsauthor() {
        return isauthor;
    }

    public void setIsauthor(Boolean isauthor) {
        this.isauthor = isauthor;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreatetime() {
        return createtime;
    }

    public void setCreatetime(LocalDateTime createtime) {
        this.createtime = createtime;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFiledownloadurl() {
        return filedownloadurl;
    }

    public void setFiledownloadurl(String filedownloadurl) {
        this.filedownloadurl = filedownloadurl;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public String getAuthoridnumber() {
        return authoridnumber;
    }

    public void setAuthoridnumber(String authoridnumber) {
        this.authoridnumber = authoridnumber;
    }

    public String getFilehash() {
        return filehash;
    }

    public void setFilehash(String filehash) {
        this.filehash = filehash;
    }

    public String getChainhash() {
        return chainhash;
    }

    public void setChainhash(String chainhash) {
        this.chainhash = chainhash;
    }

    public LocalDate getPublishedtime() {
        return publishedtime;
    }

    public void setPublishedtime(LocalDate publishedtime) {
        this.publishedtime = publishedtime;
    }

    public String getPublishedlocation() {
        return publishedlocation;
    }

    public void setPublishedlocation(String publishedlocation) {
        this.publishedlocation = publishedlocation;
    }

    public String getCopyrightownername() {
        return copyrightownername;
    }

    public void setCopyrightownername(String copyrightownername) {
        this.copyrightownername = copyrightownername;
    }

    public String getCopyrightownerid() {
        return copyrightownerid;
    }

    public void setCopyrightownerid(String copyrightownerid) {
        this.copyrightownerid = copyrightownerid;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
