package com.yi.pojo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

/**
 * @Author: gwt
 * @Date: 2020/2/23 18:05
 * @Description: 
 */
@TableName("SYS_STUDENTS")
public class SysStudents {

    @TableField(value = "CREATED_BY ")
    private String createdBy;

    /** 学生id */
    @TableId(type = IdType.UUID)
    private String id;
    /**学号**/
    private String no;

    /** 学生姓名 */
    private String name;

    /** 创建时间 */
    @TableField(value = "created_time")
    private Date createdTime;

    /** 更新人 */
    @TableField(value = "UPDATED_BY")
    private String updatedBy;

    /** 更新时间 */
    @TableField(value = "UPDATED_TIME")
    private Date updatedTime;

    /** 班级ID */
    @TableField(value = "CLASS_ID")
    private String classId;
    @TableField(value = "class_no")
    private String classNo;

    /** 家长用户id */
    @TableField(value = "PARENT_ID")
    private String parentId;

    /** 家长名称 */
    @TableField(value = "PARENT_NAME")
    private String parentName;
    @TableField(value = "CLASS_NAME")
    private String className;
    private String grade;
    @TableField(value = "CHARGE_UNAME")
    private String chargeUname;




    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getChargeUname() {
        return chargeUname;
    }

    public void setChargeUname(String chargeUname) {
        this.chargeUname = chargeUname;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }
}