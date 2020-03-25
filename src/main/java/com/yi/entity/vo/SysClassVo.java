package com.yi.entity.vo;

import com.yi.entity.PhotoWall;
import com.yi.entity.SysClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:
 * @Date: 2020/3/25 16:08
 * @Description: 
 */
public class SysClassVo extends SysClass {

    private List<PhotoWall> photos = new ArrayList<>();

    public List<PhotoWall> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoWall> photos) {
        this.photos = photos;
    }
}