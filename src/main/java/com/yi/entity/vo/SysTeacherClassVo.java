package com.yi.entity.vo;

import com.yi.entity.SysTeacherClass;

/**
 * 用于传递给前端的教师-班级关联信息
 */
public class SysTeacherClassVo extends SysTeacherClass {

    private String teacherName;

    private String lesson;

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }
}
