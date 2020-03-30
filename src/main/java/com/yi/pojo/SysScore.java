package com.yi.pojo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

/**
 * @Author: gwt
 * @Date: 2020/2/23 18:01
 * @Description: 
 */
@TableName("SYS_SCORE")
public class SysScore {

    /** 创建人 */
    @TableField(value = "CREATED_BY ")
    private String createdBy;

    /** 创建时间 */
    @TableField(value = "CREATED_TIME")
    private Date createdTime;

    /** 更新人 */
    @TableField(value = "UPDATED_BY ")
    private String updatedBy;

    /** 更新时间 */
    @TableField(value = "UPDATED_TIME")
    private Date updatedTime;

    /** 主键 */
    @TableId(type = IdType.UUID)
    private String id;

    /** 分数 */
    private Double score;

    /** 教师评语 */
    private String comment;

    @TableField("class_id")
    private String classId;

    @TableField("class_name")
    private String className;
    @TableField("stu_name")
    private String stuName;
    @TableField("stu_no")
    private String stuNo;
    @TableField("parent_id")
    private String parentId;
    @TableField("exam_id")
    private String examId;
    @TableField("exam_title")
    private String examTitle;
    @TableField("lesson_name")
    private String lessonName;
    @TableField("lesson_code")
    private String lessonCode;

    @TableField("created_id")
    private String createdId;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
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

    public String getCreatedId() {
        return createdId;
    }

    public void setCreatedId(String createdId) {
        this.createdId = createdId;
    }
}