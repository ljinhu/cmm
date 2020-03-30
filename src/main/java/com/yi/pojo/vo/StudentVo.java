package com.yi.pojo.vo;

import com.yi.pojo.SysStudents;

/**
 * @Author:
 * @Date: 2020/3/12 21:21
 * @Description: 用于接受前端数据的vo类
 */
public class StudentVo extends SysStudents {

    private String password;

    private String phone;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}