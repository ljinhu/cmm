package com.yi.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

/**
 * @Author: gwt
 * @Date: 2020/2/23 17:34
 * @Description: 家庭作业实体类
 */
@TableName("SYS_HOME_WORK")
public class SysHomeWork {

    /** 主键 */
    @TableId(type = IdType.UUID)
    private String id;

    /** 创建人 */
    @TableField(value = "CREATED_BY ")
    private String createdBy;

    /** 创建时间 */
    @TableField(value = "CREATED_TIME")
    private Date createdTime;

    /** 班级ID */
    @TableField(value = "CLASS_ID ")
    private String classId;

    /** 标题 */
    private String title;

    /** 描述 */
    private String detail;

    /** 附件，存放文件地址 */
    private String affix;

    /**
     * 附件名称，文件原名
     */
    @TableField("file_name")
    private String fileName;

    @TableField("class_name")
    private String className;

    @TableField("lesson_name")
    private String lessonName;

    @TableField("lesson_code")
    private String lessonCode;
    @TableField("create_name")
    private String createName;

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

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getLessonCode() {
        return lessonCode;
    }

    public void setLessonCode(String lessonCode) {
        this.lessonCode = lessonCode;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }
}