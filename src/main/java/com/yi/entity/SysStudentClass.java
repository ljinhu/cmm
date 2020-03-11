package com.yi.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * @Author:
 * @Date: 2020/3/10 22:31
 * @Description: 
 */
@TableName("SYS_STUDENT_CLASS")
public class SysStudentClass {

    /** 主键 */
    @TableId(type = IdType.UUID)
    private String id;
    /** 教师用户ID 教师用户ID */
    @TableField(value = "STU_ID")
    private String stuId;

    /** 班级ID 班级ID */
    @TableField("CLASS_ID")
    private String classId;


    /** 是否有效 是否有效 */
    @TableField(value = "IS_VALID ")
    private Long isValid;

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
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
}