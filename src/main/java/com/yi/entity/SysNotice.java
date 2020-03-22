package com.yi.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

/**
 * 公告
 */
@TableName("sys_notice")
public class SysNotice {

    @TableId(type= IdType.UUID)
    private String id;

    @TableField("class_no")
    private String classNo;

    @TableField("CREATED_BY")
    private String createdBy;

    @TableField("CREATED_TIME")
    private Date createdTime;

    @TableField("notice_detail")
    private String detail;

    private String affix;

    @TableField("created_name")
    private String createdName;

    private String name;

    @TableField("class_name")
    private String className;

    @TableField("affix_name")
    private String affixName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAffix() {
        return affix;
    }

    public void setAffix(String affix) {
        this.affix = affix;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAffixName() {
        return affixName;
    }

    public void setAffixName(String affixName) {
        this.affixName = affixName;
    }
}
