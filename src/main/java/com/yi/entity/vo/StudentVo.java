package com.yi.entity.vo;

import com.yi.entity.SysStudents;

/**
 * @Author:
 * @Date: 2020/3/12 21:21
 * @Description: 用于接受前端数据的vo类
 */
public class StudentVo extends SysStudents {

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}