package com.yi.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * @Author: gwt
 * @Date: 2020/2/23 18:12
 * @Description: 
 */
@TableName("SYS_TEACHER_CLASS")
public class SysTeacherClass {

    /** 教师用户ID 教师用户ID */
    private String userId;

    /** 班级ID 班级ID */
    private String classId;

    /** LESSON_NAME 课程名称 */
    @TableField(value = "LESSON_NAME")
    private String lessonName;
}