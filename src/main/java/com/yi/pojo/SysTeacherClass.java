package com.yi.pojo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * @Author: gwt
 * @Date: 2020/2/23 18:12
 * @Description: 
 */
@TableName("SYS_TEACHER_CLASS")
public class SysTeacherClass {

    /** 主键 */
    @TableId(type = IdType.UUID)
    private String id;

    /** 教师用户ID 教师用户ID */
    @TableField(value = "USER_ID")
    private String userId;

    /** 班级ID 班级ID */
    @TableField(value = "CLASS_ID")
    private String classId;

    /** LESSON_NAME 课程名称 */
    @TableField(value = "LESSON_NAME")
    private String lessonName;

    /** 是否有效 是否有效 1有效-1失效*/
    @TableField(value = "IS_VALID ")
    private Long isValid;

    @TableField("lesson_code")
    private String lessonCode;

    @TableField("class_name")
    private String className;

    public String getLessonCode() {
        return lessonCode;
    }

    public void setLessonCode(String lessonCode) {
        this.lessonCode = lessonCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public Long getIsValid() {
        return isValid;
    }

    public void setIsValid(Long isValid) {
        this.isValid = isValid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}