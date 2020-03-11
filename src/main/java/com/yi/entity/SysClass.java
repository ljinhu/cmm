package com.yi.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

/**
 * @Author: gwt
 * @Date: 2020/2/23 18:15
 * @Description: 
 */
@TableName("SYS_CLASS")
public class SysClass {
    /** 主键 */
    @TableId(type = IdType.UUID)
    private String id;

    /** 创建人 */
    @TableField(value = "CREATED_BY")
    private String createdBy;

    /** 创建时间 */
    @TableField(value = "CREATED_TIME")
    private Date createdTime;

    /** 班级名称 班级名称 */
    private String name;

    /** 是否有效 是否有效 */
    @TableField(value = "IS_VALID ")
    private Long isValid = 1L;

    /** 有效期截至时间 有效期截至时间 */
    @TableField(value = "VALID_END_TIME")
    private Date validEndTime;

    /** 班级位置 班级位置 */
    private String location;

    /** 班级描述 班级描述 */
    private String description;

    /** 班主任id 班主任id */
    @TableField(value = "CHARGE_UID ")
    private String chargeUid;

    /** 班主任名称 班主任名称 */
    @TableField(value = "CHARGE_UNAME ")
    private String chargeUname;

    /** 年级 年级 */
    private String grade;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIsValid() {
        return isValid;
    }

    public void setIsValid(Long isValid) {
        this.isValid = isValid;
    }

    public Date getValidEndTime() {
        return validEndTime;
    }

    public void setValidEndTime(Date validEndTime) {
        this.validEndTime = validEndTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChargeUid() {
        return chargeUid;
    }

    public void setChargeUid(String chargeUid) {
        this.chargeUid = chargeUid;
    }

    public String getChargeUname() {
        return chargeUname;
    }

    public void setChargeUname(String chargeUname) {
        this.chargeUname = chargeUname;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}