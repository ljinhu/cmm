package com.yi.pojo.vo;

import com.yi.pojo.PhotoWall;
import com.yi.pojo.SysClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:
 * @Date: 2020/3/25 16:08
 * @Description: 
 */
public class SysClassVo extends SysClass {

    private List<PhotoWall> photos = new ArrayList<>();
    private Integer students = 0;
    public List<PhotoWall> getPhotos() {
        return photos;
    }

    private Integer teachers = 0;

    public void setPhotos(List<PhotoWall> photos) {
        this.photos = photos;
    }

    public Integer getStudents() {
        return students;
    }

    public void setStudents(Integer students) {
        this.students = students;
    }

    public Integer getTeachers() {
        return teachers;
    }

    public void setTeachers(Integer teachers) {
        this.teachers = teachers;
    }


}