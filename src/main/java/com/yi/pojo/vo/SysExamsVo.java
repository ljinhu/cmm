package com.yi.pojo.vo;

import com.yi.pojo.SysExams;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:
 * @Date: 2020/3/27 15:37
 * @Description: 
 */
public class SysExamsVo extends SysExams {

    private List<String> lessons = new ArrayList<>();

    public List<String> getLessons() {
        return lessons;
    }

    public void setLessons(List<String> lessons) {
        this.lessons = lessons;
    }
}