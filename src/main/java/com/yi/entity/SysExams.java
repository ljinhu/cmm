package com.yi.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;


@TableName("sys_exams")
public class SysExams {

    @TableId(type = IdType.UUID)
    private String id;

    @TableField("CREATED_ID")
    private String createdId;

    @TableField("CREATED_TIME")
    private Date createdTime;

    @TableField("Create_name")
    private String createName;

    private String code;

    private String title;

    private String detail;

    private Date begin;

    private Date end;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @TableField("lesson_Name")
    private String lessonName;

    @TableField("lesson_Code")
    private String lessonCode;

    @TableField("class_id")
    private String classId;

    private String grade;

    @TableField("class_name")
    private String className;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedId() {
        return createdId;
    }

    public void setCreatedId(String createdId) {
        this.createdId = createdId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
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

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
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

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
